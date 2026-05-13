<template>
  <div class="portrait-page">
    <div class="page-header">
      <h2 class="page-title">数字画像</h2>
      <el-select v-if="isAdmin" v-model="selectedUserId" placeholder="选择教师" filterable style="width:220px" @change="onUserChange">
        <el-option v-for="u in teacherList" :key="u.id" :label="u.name + ' (' + u.college + ')'" :value="u.id" />
      </el-select>
      <span v-else class="current-user">{{ userStore.userName || '当前用户' }}</span>
    </div>

    <el-row :gutter="16" class="summary-row">
      <el-col :span="4">
        <el-card shadow="hover" class="summary-card">
          <div class="card-value">{{ fmtMoney(dashboard.totalFunding) }}</div>
          <div class="card-label">项目总经费(万)</div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card shadow="hover" class="summary-card">
          <div class="card-value">{{ dashboard.paperACount }} / {{ dashboard.paperBCount }}</div>
          <div class="card-label">A/B类论文</div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card shadow="hover" class="summary-card">
          <div class="card-value">{{ dashboard.patentGrantedCount }}</div>
          <div class="card-label">已授权专利</div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card shadow="hover" class="summary-card">
          <div class="card-value">{{ dashboard.softwareCount }}</div>
          <div class="card-label">软件著作</div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card shadow="hover" class="summary-card">
          <div class="card-value">{{ dashboard.competitionAwardCount }}</div>
          <div class="card-label">竞赛获奖</div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card shadow="hover" class="summary-card">
          <div class="card-value" :style="{ color: scoreColor(avgScore) }">{{ avgScore.toFixed(1) }}</div>
          <div class="card-label">综合均分</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" style="margin-top:16px">
      <el-col :span="12">
        <el-card shadow="never">
          <template #header><span class="card-title">综合能力雷达图</span></template>
          <RadarChart ref="radarRef" :radar-data="radarData" :compare-mode="compareMode" :compare-data="compareData" :teacher-list="teacherList" @update:compare-data="onCompareData" />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <template #header><span class="card-title">成果分布</span></template>
          <DistributionChart :user-id="selectedUserId" />
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" style="margin-top:16px">
      <el-col :span="24">
        <el-card shadow="never">
          <template #header><span class="card-title">历年趋势</span></template>
          <TrendChart :trend-data="trendData" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '../../store/user'
import { getPortraitRadar, getPortraitDashboard, getPortraitTrend, getPortraitTeachers } from '../../api/portrait'
import RadarChart from './RadarChart.vue'
import TrendChart from './TrendChart.vue'
import DistributionChart from './DistributionChart.vue'

const route = useRoute()
const userStore = useUserStore()
const isAdmin = computed(() => userStore.role === 'ADMIN')
const currentUserId = computed(() => userStore.userInfo?.userId)

const selectedUserId = ref(null)
const teacherList = ref([])
const radarData = ref(null)
const dashboard = ref({ totalFunding: 0, paperACount: 0, paperBCount: 0, patentGrantedCount: 0, softwareCount: 0, competitionAwardCount: 0, rawScores: {}, normalizedScores: {} })
const trendData = ref([])
const compareMode = ref(false)
const compareData = ref(null)
const radarRef = ref(null)

const avgScore = computed(() => {
  const ns = dashboard.value.normalizedScores || {}
  const dims = ['科研项目', '专利成果', '软件著作', '学术论文', '竞赛指导']
  const sum = dims.reduce((s, d) => s + Number(ns[d] || 0), 0)
  return dims.length > 0 ? sum / dims.length : 0
})

function scoreColor(v) { return v >= 60 ? '#67C23A' : v >= 30 ? '#E6A23C' : '#F56C6C' }

function fmtMoney(v) { return v ? Number(v).toFixed(1) : '0.0' }

async function loadTeachers() {
  try {
    const res = await getPortraitTeachers()
    teacherList.value = res.data || []
  } catch { /* ignore */ }
}

async function loadData(uid) {
  try {
    const [radarRes, dashRes, trendRes] = await Promise.all([
      getPortraitRadar(uid), getPortraitDashboard(uid), getPortraitTrend(uid)
    ])
    if (radarRes.code === 200) radarData.value = radarRes.data
    if (dashRes.code === 200) dashboard.value = dashRes.data
    if (trendRes.code === 200) trendData.value = trendRes.data
  } catch { /* ignore */ }
}

function onUserChange(uid) {
  compareMode.value = false
  compareData.value = null
  loadData(uid)
}

function onCompareData(data) {
  compareData.value = data
}

onMounted(async () => {
  const routeUserId = route.params.userId
  if (isAdmin.value) {
    await loadTeachers()
    selectedUserId.value = routeUserId ? Number(routeUserId) : (teacherList.value[0]?.id || currentUserId.value)
  } else {
    selectedUserId.value = currentUserId.value
  }
  if (selectedUserId.value) loadData(selectedUserId.value)
})

watch(() => route.params.userId, (val) => {
  if (val) { selectedUserId.value = Number(val); loadData(Number(val)) }
})
</script>

<style scoped>
.portrait-page { padding: 0; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.page-title { font-size: 20px; color: #303133; }
.current-user { font-size: 16px; color: #409EFF; font-weight: bold; }
.summary-row { margin-bottom: 0; }
.summary-card { text-align: center; cursor: default; }
.summary-card .card-value { font-size: 28px; font-weight: bold; color: #303133; }
.summary-card .card-label { font-size: 13px; color: #909399; margin-top: 4px; }
.card-title { font-size: 15px; font-weight: bold; color: #303133; }
</style>
