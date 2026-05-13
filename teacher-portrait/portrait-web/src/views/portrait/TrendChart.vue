<template>
  <div class="trend-container">
    <el-radio-group v-model="activeTab" size="small" @change="refreshChart">
      <el-radio-button label="project">项目数量</el-radio-button>
      <el-radio-button label="patent">专利</el-radio-button>
      <el-radio-button label="software">软著</el-radio-button>
      <el-radio-button label="paper">论文</el-radio-button>
      <el-radio-button label="competition">竞赛</el-radio-button>
    </el-radio-group>
    <div ref="chartRef" class="chart"></div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch, nextTick } from 'vue'
import * as echarts from 'echarts'

const props = defineProps({ trendData: Array })
const activeTab = ref('project')
const chartRef = ref(null)
let chartInstance = null

function getField(tab) {
  const map = { project: 'projectCount', patent: 'patentCount', software: 'softwareCount', paper: 'paperCount', competition: 'competitionCount' }
  return map[tab] || 'projectCount'
}

function buildOption() {
  if (!props.trendData || props.trendData.length === 0) return {}
  const years = props.trendData.map(d => d.year)
  const field = getField(activeTab.value)
  const vals = props.trendData.map(d => Number(d[field] || 0))

  const series = [{
    name: '数量', type: 'bar', data: vals,
    itemStyle: { color: '#5470C6', borderRadius: [4, 4, 0, 0] }
  }]
  const yAxes = [{ type: 'value', name: '数量' }]

  if (activeTab.value === 'project') {
    const funds = props.trendData.map(d => Number(d.projectFunding || 0))
    series.push({
      name: '经费(万元)', type: 'line', yAxisIndex: 1,
      data: funds, lineStyle: { color: '#EE6666' }, itemStyle: { color: '#EE6666' }
    })
    yAxes.push({ type: 'value', name: '经费(万元)' })
  }

  return {
    tooltip: { trigger: 'axis' },
    legend: { bottom: 0, data: series.map(s => s.name) },
    grid: { left: 50, right: 50, top: 20, bottom: 40 },
    xAxis: { type: 'category', data: years },
    yAxis: yAxes,
    series
  }
}

function initChart() {
  if (!chartRef.value || !props.trendData) return
  if (chartInstance) chartInstance.dispose()
  chartInstance = echarts.init(chartRef.value)
  chartInstance.setOption(buildOption())
}

function refreshChart() { if (chartInstance) chartInstance.setOption(buildOption(), true) }

watch(() => props.trendData, () => nextTick(initChart), { deep: true })
watch(activeTab, refreshChart)

onMounted(() => { nextTick(initChart) })
onUnmounted(() => { if (chartInstance) chartInstance.dispose() })
</script>

<style scoped>
.trend-container { width: 100%; }
.trend-container > .el-radio-group { margin-bottom: 10px; }
.chart { width: 100%; height: 350px; }
</style>
