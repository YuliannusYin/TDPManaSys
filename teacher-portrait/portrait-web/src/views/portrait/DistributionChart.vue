<template>
  <div class="dist-container">
    <el-radio-group v-model="activeTab" size="small" @change="loadData">
      <el-radio-button label="projectLevel">项目级别</el-radio-button>
      <el-radio-button label="patentType">专利类型</el-radio-button>
      <el-radio-button label="paperClass">论文类别</el-radio-button>
      <el-radio-button label="compLevel">竞赛级别</el-radio-button>
    </el-radio-group>
    <div ref="chartRef" class="chart"></div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch, nextTick } from 'vue'
import * as echarts from 'echarts'
import request from '../../api/request'

const props = defineProps({ userId: Number })
const activeTab = ref('projectLevel')
const chartRef = ref(null)
let chartInstance = null

const COLORS = ['#5470C6', '#91CC75', '#FAC858', '#EE6666', '#73C0DE', '#FC8452', '#9A60B4', '#3BA272']

async function loadData() {
  if (!props.userId) return
  try {
    const res = await request({ url: `/portrait/${props.userId}/distribution`, method: 'get' })
    if (res.code !== 200) return
    const dist = res.data
    let data = []
    switch (activeTab.value) {
      case 'projectLevel': data = dist.projectLevel || []; break
      case 'patentType': data = dist.patentType || []; break
      case 'paperClass': data = dist.paperClass || []; break
      case 'compLevel': data = dist.competitionLevel || []; break
    }
    renderChart(data)
  } catch { }
}

function renderChart(data) {
  if (!chartRef.value) return
  if (chartInstance) chartInstance.dispose()
  chartInstance = echarts.init(chartRef.value)
  if (!data || data.length === 0) {
    chartInstance.setOption({
      title: { text: '暂无数据', left: 'center', top: 'center', textStyle: { color: '#999', fontSize: 14 } }
    })
    return
  }
  chartInstance.setOption({
    tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
    legend: { orient: 'vertical', right: 10, top: 'center' },
    series: [{
      type: 'pie',
      radius: ['40%', '70%'],
      center: ['40%', '50%'],
      avoidLabelOverlap: true,
      itemStyle: { borderRadius: 4, borderColor: '#fff', borderWidth: 2 },
      label: { show: false },
      emphasis: { label: { show: true, fontSize: 16, fontWeight: 'bold' } },
      data: data.map((d, i) => ({ ...d, itemStyle: { color: COLORS[i % COLORS.length] } }))
    }]
  })
}

watch(() => props.userId, () => { nextTick(loadData) })
onMounted(() => { nextTick(loadData) })
onUnmounted(() => { if (chartInstance) chartInstance.dispose() })
</script>

<style scoped>
.dist-container { width: 100%; }
.dist-container > .el-radio-group { margin-bottom: 10px; }
.chart { width: 100%; height: 320px; }
</style>