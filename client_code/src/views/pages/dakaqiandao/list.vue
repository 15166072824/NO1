<template>
  <div class="daka-container">
    <div class="daka-header">
      <h3>医生工作打卡</h3>
      <div class="current-time">{{ currentTime }}</div>
    </div>
    
    <div class="daka-status">
      <el-card class="status-card">
        <div class="status-item">
          <span class="status-label">今日状态：</span>
          <span class="status-value" :class="statusClass">{{ todayStatus }}</span>
        </div>
        <div class="status-item" v-if="workDuration > 0">
          <span class="status-label">工作时长：</span>
          <span class="status-value">{{ formatDuration(workDuration) }}</span>
        </div>
        <div class="status-item" v-if="pauseDuration > 0">
          <span class="status-label">暂停时长：</span>
          <span class="status-value">{{ formatDuration(pauseDuration) }}</span>
        </div>
      </el-card>
    </div>
    
    <div class="daka-buttons">
      <el-button 
        type="success" 
        size="large" 
        @click="shangbanDaka"
        :disabled="status.shangban"
        class="daka-btn">
        <i class="el-icon-time"></i>
        上班打卡
      </el-button>
      
      <el-button 
        type="warning" 
        size="large" 
        @click="zantingDaka"
        :disabled="!status.shangban || status.xiaban"
        class="daka-btn">
        <i class="el-icon-pause"></i>
        {{ status.zantingzhong ? '结束暂停' : '开始暂停' }}
      </el-button>
      
      <el-button 
        type="danger" 
        size="large" 
        @click="xiabanDaka"
        :disabled="!status.shangban || status.xiaban"
        class="daka-btn">
        <i class="el-icon-switch-button"></i>
        下班打卡
      </el-button>
    </div>
    
    <div class="daka-history" v-if="dakaList.length > 0">
      <h4>今日打卡记录</h4>
      <el-table :data="dakaList" style="width: 100%">
        <el-table-column prop="dakazhuangtai" label="打卡类型" width="120">
          <template #default="scope">
            <el-tag :type="getTagType(scope.row.dakazhuangtai)">{{ scope.row.dakazhuangtai }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="dakashijian" label="打卡时间" width="180">
          <template #default="scope">
            {{ formatTime(scope.row.dakashijian) }}
          </template>
        </el-table-column>
        <el-table-column prop="beizhu" label="备注" />
        <el-table-column prop="gongzuoshichang" label="工作时长" width="120">
          <template #default="scope">
            <span v-if="scope.row.gongzuoshichang > 0">{{ formatDuration(scope.row.gongzuoshichang) }}</span>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted, onUnmounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'
import toolUtil from '@/utils/toolUtil'

export default {
  name: 'DakaqiandaoList',
  setup() {
    const currentTime = ref('')
    const status = reactive({
      shangban: false,
      zantingzhong: false,
      xiaban: false
    })
    const workDuration = ref(0)
    const pauseDuration = ref(0)
    const dakaList = ref([])
    let timer = null
    
    const todayStatus = computed(() => {
      if (status.xiaban) return '已下班'
      if (status.zantingzhong) return '暂停中'
      if (status.shangban) return '工作中'
      return '未打卡'
    })
    
    const statusClass = computed(() => {
      if (status.xiaban) return 'status-off'
      if (status.zantingzhong) return 'status-pause'
      if (status.shangban) return 'status-work'
      return 'status-none'
    })
    
    const updateTime = () => {
      const now = new Date()
      currentTime.value = now.toLocaleString('zh-CN')
      
      // 实时计算工作时长
      if (status.shangban && !status.xiaban && !status.zantingzhong) {
        const user = toolUtil.storageGet('user')
        if (user && user.yishengzhanghao) {
          // 获取今天的上班打卡记录
          const today = new Date().toISOString().split('T')[0]
          const shangbanRecord = dakaList.value.find(item => {
            const itemDate = new Date(item.dakashijian).toISOString().split('T')[0]
            return itemDate === today && item.dakazhuangtai === '上班'
          })
          
          if (shangbanRecord) {
            const shangbanTime = new Date(shangbanRecord.dakashijian).getTime()
            const currentTime = new Date().getTime()
            const totalMinutes = Math.floor((currentTime - shangbanTime) / (1000 * 60))
            
            // 减去暂停时长
            const pauseMinutes = shangbanRecord.zantingzongshichang || 0
            const actualMinutes = totalMinutes - pauseMinutes
            
            workDuration.value = actualMinutes > 0 ? actualMinutes : 0
          }
        }
      }
    }
    
    const formatTime = (time) => {
      if (!time) return ''
      return new Date(time).toLocaleString('zh-CN')
    }
    
    const formatDuration = (minutes) => {
      if (!minutes || minutes <= 0) return '0分钟'
      const hours = Math.floor(minutes / 60)
      const mins = minutes % 60
      if (hours > 0) {
        return `${hours}小时${mins}分钟`
      }
      return `${mins}分钟`
    }
    
    const getTagType = (status) => {
      switch (status) {
        case '上班': return 'success'
        case '暂停': return 'warning'
        case '下班': return 'danger'
        default: return 'info'
      }
    }
    
    const getTodayDakaStatus = async () => {
      try {
        const user = toolUtil.storageGet('user')
        if (!user || !user.yishengzhanghao) {
          ElMessage.error('请先登录')
          return
        }
        
        const response = await axios.get('/api/dakaqiandao/getTodayDakaStatus', {
          params: { yishengzhanghao: user.yishengzhanghao }
        })
        
        if (response.data.code === 0) {
          const data = response.data.data
          status.shangban = data.shangban
          status.zantingzhong = data.zantingzhong
          status.xiaban = data.xiaban
          
          if (data.shangbanData) {
            workDuration.value = data.shangbanData.gongzuoshichang || 0
            pauseDuration.value = data.shangbanData.zantingzongshichang || 0
          }
          
          if (data.xiabanData) {
            workDuration.value = data.xiabanData.gongzuoshichang || 0
          }
          
          await getTodayDakaList()
        }
      } catch (error) {
        ElMessage.error('获取打卡状态失败')
      }
    }
    
    const getTodayDakaList = async () => {
      try {
        const user = toolUtil.storageGet('user')
        if (!user || !user.yishengzhanghao) return
        
        const response = await axios.get('/api/dakaqiandao/list', {
          params: {
            yishengzhanghao: user.yishengzhanghao,
            sort: 'dakashijian',
            order: 'desc'
          }
        })
        
        if (response.data.code === 0) {
          const today = new Date().toISOString().split('T')[0]
          dakaList.value = response.data.data.list.filter(item => {
            const itemDate = new Date(item.dakashijian).toISOString().split('T')[0]
            return itemDate === today
          })
        }
      } catch (error) {
        console.error('获取打卡记录失败', error)
      }
    }
    
    const shangbanDaka = async () => {
      try {
        const user = toolUtil.storageGet('user')
        if (!user || !user.yishengzhanghao) {
          ElMessage.error('请先登录')
          return
        }
        
        const response = await axios.post('/api/dakaqiandao/shangbanDaka', {
          yishengzhanghao: user.yishengzhanghao
        })
        
        if (response.data.code === 0) {
          ElMessage.success('上班打卡成功')
          await getTodayDakaStatus()
        } else {
          ElMessage.error(response.data.msg)
        }
      } catch (error) {
        ElMessage.error('打卡失败')
      }
    }
    
    const zantingDaka = async () => {
      try {
        const user = toolUtil.storageGet('user')
        if (!user || !user.yishengzhanghao) {
          ElMessage.error('请先登录')
          return
        }
        
        const zhuangtai = status.zantingzhong ? '暂停结束' : '暂停开始'
        
        const response = await axios.post('/api/dakaqiandao/zantingDaka', {
          yishengzhanghao: user.yishengzhanghao,
          zhuangtai: zhuangtai
        })
        
        if (response.data.code === 0) {
          ElMessage.success(zhuangtai + '成功')
          await getTodayDakaStatus()
        } else {
          ElMessage.error(response.data.msg)
        }
      } catch (error) {
        ElMessage.error('操作失败')
      }
    }
    
    const xiabanDaka = async () => {
      try {
        const user = toolUtil.storageGet('user')
        if (!user || !user.yishengzhanghao) {
          ElMessage.error('请先登录')
          return
        }
        
        const response = await axios.post('/api/dakaqiandao/xiabanDaka', {
          yishengzhanghao: user.yishengzhanghao
        })
        
        if (response.data.code === 0) {
          ElMessage.success(response.data.msg)
          await getTodayDakaStatus()
        } else {
          ElMessage.error(response.data.msg)
        }
      } catch (error) {
        ElMessage.error('打卡失败')
      }
    }
    
    onMounted(() => {
      updateTime()
      timer = setInterval(updateTime, 1000)
      getTodayDakaStatus()
    })
    
    onUnmounted(() => {
      if (timer) {
        clearInterval(timer)
      }
    })
    
    return {
      currentTime,
      status,
      workDuration,
      pauseDuration,
      dakaList,
      todayStatus,
      statusClass,
      formatTime,
      formatDuration,
      getTagType,
      shangbanDaka,
      zantingDaka,
      xiabanDaka
    }
  }
}
</script>

<style scoped>
.daka-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.daka-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 2px solid #f0f0f0;
}

.daka-header h3 {
  margin: 0;
  color: #333;
  font-size: 24px;
}

.current-time {
  font-size: 18px;
  color: #666;
  font-weight: 500;
}

.daka-status {
  margin-bottom: 30px;
}

.status-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
}

.status-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.status-item:last-child {
  margin-bottom: 0;
}

.status-label {
  font-size: 16px;
  opacity: 0.9;
}

.status-value {
  font-size: 18px;
  font-weight: bold;
}

.status-none { color: #ccc; }
.status-work { color: #67c23a; }
.status-pause { color: #e6a23c; }
.status-off { color: #f56c6c; }

.daka-buttons {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-bottom: 40px;
}

.daka-btn {
  min-width: 140px;
  height: 60px;
  font-size: 18px;
  border-radius: 30px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
}

.daka-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.2);
}

.daka-btn:disabled {
  opacity: 0.6;
  transform: none;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.daka-history {
  margin-top: 40px;
}

.daka-history h4 {
  margin-bottom: 20px;
  color: #333;
  font-size: 20px;
}

@media (max-width: 768px) {
  .daka-buttons {
    flex-direction: column;
    align-items: center;
  }
  
  .daka-btn {
    width: 100%;
    max-width: 300px;
  }
}
</style>