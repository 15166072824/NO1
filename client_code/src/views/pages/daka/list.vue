<template>
	<div class="list-page" :style='{}'>
        <div class="breadcrumb-wrapper" style="width: 100%;">
            <div class="bread_view">
                <el-breadcrumb separator="Ξ" class="breadcrumb">
                    <el-breadcrumb-item class="first_breadcrumb" :to="{ path: '/' }">首页</el-breadcrumb-item>
                    <el-breadcrumb-item class="second_breadcrumb" v-for="(item,index) in breadList" :key="index">{{item.name}}</el-breadcrumb-item>
                </el-breadcrumb>
            </div>
        </div>
		<div class="daka-container">
            <div class="daka-status">
                <h2>当前状态：{{ currentStatus }}</h2>
                <div class="status-time">
                    <span v-if="currentStatus === '已打卡上班'">上班时间：{{ workStartTime }}</span>
                    <span v-if="currentStatus === '暂停工作'">暂停时间：{{ pauseStartTime }}</span>
                </div>
            </div>
            <div class="daka-buttons">
                <el-button v-if="currentStatus === '未打卡'" type="primary" size="large" @click="dakaStart">
                    打卡上班
                </el-button>
                <el-button v-if="currentStatus === '已打卡上班'" type="warning" size="large" @click="dakaPause">
                    暂停工作
                </el-button>
                <el-button v-if="currentStatus === '暂停工作'" type="success" size="large" @click="dakaResume">
                    恢复工作
                </el-button>
                <el-button v-if="currentStatus !== '未打卡'" type="danger" size="large" @click="dakaEnd">
                    打卡下班
                </el-button>
            </div>
            <div class="daka-record">
                <h3>今日打卡记录</h3>
                <el-table :data="recordList" border>
                    <el-table-column label="打卡类型" prop="type" />
                    <el-table-column label="打卡时间" prop="time" />
                    <el-table-column label="时长" prop="duration" />
                </el-table>
            </div>
        </div>
	</div>
</template>

<script setup>
	import {
		ref,
		getCurrentInstance,
		computed
	} from 'vue';
	import {
		useStore
	} from 'vuex';
	import moment from 'moment'
    const store = useStore()
    const user = computed(()=>store.getters['user/session'])
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	
	//基础信息
	const tableName = 'daka'
	const formName = '打卡管理'
	const breadList = ref([{
		name: formName
	}])
	
	//打卡状态
	const currentStatus = ref('未打卡')
	const workStartTime = ref('')
	const pauseStartTime = ref('')
	const recordList = ref([])
	
	//打卡上班
	const dakaStart = () => {
		context?.$http({
			url: 'daka/start',
			method: 'post',
			data: {
				userId: user.value.id,
				userType: user.value.role
			}
		}).then(res => {
			if (res.data.code === 200) {
				context?.$toolUtil.message('打卡上班成功', 'success')
				currentStatus.value = '已打卡上班'
				workStartTime.value = moment().format('YYYY-MM-DD HH:mm:ss')
				recordList.value.push({
					type: '打卡上班',
					time: workStartTime.value,
					duration: ''
				})
			} else {
				context?.$toolUtil.message(res.data.msg, 'error')
			}
		})
	}
	
	//暂停工作
	const dakaPause = () => {
		context?.$http({
			url: 'daka/pause',
			method: 'post',
			data: {
				userId: user.value.id
			}
		}).then(res => {
			if (res.data.code === 200) {
				context?.$toolUtil.message('暂停工作成功', 'warning')
				currentStatus.value = '暂停工作'
				pauseStartTime.value = moment().format('YYYY-MM-DD HH:mm:ss')
				recordList.value.push({
					type: '暂停工作',
					time: pauseStartTime.value,
					duration: ''
				})
			} else {
				context?.$toolUtil.message(res.data.msg, 'error')
			}
		})
	}
	
	//恢复工作
	const dakaResume = () => {
		context?.$http({
			url: 'daka/resume',
			method: 'post',
			data: {
				userId: user.value.id
			}
		}).then(res => {
			if (res.data.code === 200) {
				context?.$toolUtil.message('恢复工作成功', 'success')
				currentStatus.value = '已打卡上班'
				recordList.value.push({
					type: '恢复工作',
					time: moment().format('YYYY-MM-DD HH:mm:ss'),
					duration: moment().diff(moment(pauseStartTime.value), 'minutes') + '分钟'
				})
			} else {
				context?.$toolUtil.message(res.data.msg, 'error')
			}
		})
	}
	
	//打卡下班
	const dakaEnd = () => {
		context?.$http({
			url: 'daka/end',
			method: 'post',
			data: {
				userId: user.value.id
			}
		}).then(res => {
			if (res.data.code === 200) {
				context?.$toolUtil.message('打卡下班成功', 'success')
				currentStatus.value = '未打卡'
				recordList.value.push({
					type: '打卡下班',
					time: moment().format('YYYY-MM-DD HH:mm:ss'),
					duration: moment().diff(moment(workStartTime.value), 'hours', true).toFixed(2) + '小时'
				})
			} else {
				context?.$toolUtil.message(res.data.msg, 'error')
			}
		})
	}
	
	//初始化打卡状态
	const initDakaStatus = () => {
		context?.$http({
			url: 'daka/status',
			method: 'get',
			params: {
				userId: user.value.id
			}
		}).then(res => {
			if (res.data.code === 200) {
				const data = res.data.data
				currentStatus.value = data.status
				if (data.startTime) {
					workStartTime.value = moment(data.startTime).format('YYYY-MM-DD HH:mm:ss')
				}
				if (data.pauseTime) {
					pauseStartTime.value = moment(data.pauseTime).format('YYYY-MM-DD HH:mm:ss')
				}
				recordList.value = data.records || []
			}
		})
	}
	
	const init = async() => {
		if(context.$toolUtil.storageGet('frontToken') && !user.value.id){
            await store.dispatch("user/getSession")
        }
		initDakaStatus()
	}
	init()
</script>

<style lang="scss" scoped>
	.daka-container {
		padding: 20px;
		max-width: 800px;
		margin: 0 auto;
	}
	
	.daka-status {
		background: #f5f7fa;
		padding: 20px;
		border-radius: 8px;
		margin-bottom: 20px;
		
		h2 {
			margin: 0 0 10px 0;
			color: #333;
		}
		
		.status-time {
			color: #666;
			font-size: 14px;
		}
	}
	
	.daka-buttons {
		display: flex;
		gap: 15px;
		margin-bottom: 30px;
		flex-wrap: wrap;
	}
	
	.daka-record {
		
		h3 {
			margin: 0 0 15px 0;
			color: #333;
		}
	}
</style>