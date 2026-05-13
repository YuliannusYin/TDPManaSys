<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">评分权重配置</h2>
      <el-button type="primary" :icon="Check" :disabled="totalWeight !== 100" @click="handleSave">保存配置</el-button>
    </div>

    <el-alert type="info" :closable="false" show-icon style="margin-bottom:16px">
      权重总和：<strong :style="{ color: totalWeight === 100 ? '#67C23A' : '#F56C6C' }">{{ totalWeight }}%</strong>
      <template v-if="totalWeight !== 100">
        <el-tag type="danger" size="small" style="margin-left:8px">总和必须为 100%</el-tag>
      </template>
    </el-alert>

    <el-row :gutter="16">
      <el-col v-for="item in configList" :key="item.id" :span="24" style="margin-bottom:16px">
        <el-card shadow="hover">
          <template #header>
            <div class="config-header">
              <span class="config-title">{{ item.dimension }}</span>
              <span class="config-weight">{{ item.weight }}%</span>
            </div>
          </template>
          <div class="slider-row">
            <span class="slider-label">权重</span>
            <el-slider v-model="item.weight" :min="0" :max="100" :step="1" show-input style="flex:1;margin-left:12px" />
          </div>
          <el-collapse style="margin-top:12px">
            <el-collapse-item title="计分规则详情">
              <el-input v-model="item.scoringRules" type="textarea" :rows="4" placeholder="JSON 格式计分规则" />
            </el-collapse-item>
          </el-collapse>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { Check } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import request from '../../api/request'

const configList = reactive([])

const totalWeight = computed(() => {
  const sum = configList.reduce((s, item) => s + Number(item.weight || 0), 0)
  return Math.round(sum * 100) / 100
})

onMounted(async () => {
  try {
    const res = await request({ url: '/score-config', method: 'get' })
    if (res.code === 200 && res.data) {
      configList.splice(0, configList.length, ...res.data.map(item => ({
        ...item,
        weight: Number(item.weight)
      })))
    }
  } catch { }
})

async function handleSave() {
  try {
    const payload = configList.map(item => ({
      id: item.id,
      dimension: item.dimension,
      weight: item.weight,
      scoringRules: item.scoringRules
    }))
    await request({ url: '/score-config', method: 'put', data: payload })
    ElMessage.success('配置已更新，画像数据将重新计算')
  } catch { }
}
</script>

<style scoped>
.page-container { padding: 0; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.page-title { font-size: 20px; color: #303133; }
.config-header { display: flex; justify-content: space-between; align-items: center; }
.config-title { font-size: 15px; font-weight: bold; }
.config-weight { font-size: 24px; font-weight: bold; color: #409EFF; }
.slider-row { display: flex; align-items: center; }
.slider-label { width: 40px; color: #909399; }
</style>
