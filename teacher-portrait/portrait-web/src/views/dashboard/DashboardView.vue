<template>
  <div class="dashboard">
    <div class="dash-header">
      <div>
        <h2 class="dash-title">首页仪表盘</h2>
        <p class="dash-greeting">{{ greeting }}，{{ userStore.userName }}</p>
      </div>
    </div>

    <div class="stats-grid">
      <div v-for="card in cards" :key="card.label" class="stat-card" :style="{ '--card-accent': card.accent }">
        <div class="stat-top">
          <div class="stat-icon-wrap">
            <el-icon :size="22"><component :is="card.icon" /></el-icon>
          </div>
          <span class="stat-label">{{ card.label }}</span>
        </div>
        <div class="stat-value">{{ card.value }}</div>
        <div v-if="card.sub" class="stat-sub">{{ card.sub }}</div>
      </div>
    </div>

    <el-row :gutter="20" style="margin-top: 24px;">
      <el-col :span="14">
        <el-card shadow="never" class="section-card">
          <template #header>
            <div class="section-header">
              <span class="section-title">快速入口</span>
              <span class="section-desc">常用功能快捷访问</span>
            </div>
          </template>
          <div class="quick-grid">
            <div v-for="item in quickLinks" :key="item.path" class="quick-item" @click="router.push(item.path)">
              <div class="quick-icon" :style="{ background: item.bg }">
                <el-icon :size="20" color="#fff"><component :is="item.icon" /></el-icon>
              </div>
              <span class="quick-label">{{ item.label }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never" class="section-card about-card">
          <template #header>
            <div class="section-header">
              <span class="section-title">关于系统</span>
            </div>
          </template>
          <p class="about-text">教师数字画像系统用于高校教师科研成果的统一管理与可视化数字画像展示。</p>
          <p class="about-text">通过多维度成果数据的雷达图与统计图表，直观呈现教师综合能力，服务于个人发展自评与学院统筹管理。</p>
          <div class="about-tags">
            <span class="about-tag">Spring Boot</span>
            <span class="about-tag">Vue 3</span>
            <span class="about-tag">ECharts</span>
            <span class="about-tag">MyBatis-Plus</span>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../../store/user'
import { getPortraitDashboard, getAggregatedDashboard } from '../../api/portrait'
import { Document, Collection, TrophyBase, DataBoard, Money, Files, FolderOpened, Reading, PieChart } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()
const dashboard = ref({})

const greeting = computed(() => {
  const h = new Date().getHours()
  if (h < 6) return '夜深了'
  if (h < 12) return '上午好'
  if (h < 14) return '中午好'
  if (h < 18) return '下午好'
  return '晚上好'
})

const portraitPath = computed(() => {
  return userStore.userInfo ? `/portrait/${userStore.userInfo.userId}` : '/portrait'
})

function fmtMoney(v) { return v ? Number(v).toFixed(1) : '0.0' }

const cards = computed(() => {
  const d = dashboard.value
  return [
    { label: '项目总数', value: d.projectTotalCount ?? '-', icon: Document, accent: 'var(--color-blue)' },
    { label: '项目总经费(万)', value: fmtMoney(d.totalFunding), icon: Money, accent: 'var(--color-teal)' },
    { label: '已授权专利', value: d.patentGrantedCount ?? '-', icon: TrophyBase, accent: 'var(--color-amber)' },
    { label: '软件著作', value: d.softwareCount ?? '-', icon: Files, accent: 'var(--color-info)' },
    { label: '学术论文', value: d.paperTotalCount ?? '-', sub: d.paperACount != null ? `A类 ${d.paperACount} / B类 ${d.paperBCount}` : '', icon: DataBoard, accent: 'var(--color-rose)' },
    { label: '竞赛获奖', value: d.competitionAwardCount ?? '-', icon: Collection, accent: 'var(--color-success)' }
  ]
})

const quickLinks = [
  { label: '纵向项目', path: '/project/vertical', icon: FolderOpened, bg: 'var(--color-blue)' },
  { label: '横向项目', path: '/project/horizontal', icon: FolderOpened, bg: 'var(--color-teal)' },
  { label: '专利管理', path: '/patent', icon: TrophyBase, bg: 'var(--color-amber)' },
  { label: '论文管理', path: '/paper', icon: Reading, bg: 'var(--color-rose)' },
  { label: '数字画像', path: '', icon: PieChart, bg: 'var(--color-primary)' },
]

const quickLinksResolved = computed(() =>
  quickLinks.map(item => ({
    ...item,
    path: item.label === '数字画像' ? portraitPath.value : item.path
  }))
)

onMounted(async () => {
  try {
    let res
    if (userStore.role === 'ADMIN') {
      res = await getAggregatedDashboard()
    } else if (userStore.userInfo?.userId) {
      res = await getPortraitDashboard(userStore.userInfo.userId)
    }
    if (res && res.code === 200) {
      dashboard.value = res.data
    }
  } catch { /* ignore */ }
})
</script>

<style scoped>
.dashboard {
  max-width: 1200px;
}

.dash-header {
  margin-bottom: 24px;
}

.dash-title {
  font-family: var(--font-display);
  font-size: 22px;
  font-weight: 700;
  color: var(--color-primary);
  margin-bottom: 4px;
}

.dash-greeting {
  font-size: 14px;
  color: var(--color-text-muted);
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 16px;
}

.stat-card {
  background: var(--color-card);
  border: 1px solid var(--color-border-light);
  border-radius: var(--radius-md);
  padding: 20px;
  transition: all var(--transition-base);
  position: relative;
  overflow: hidden;
}

.stat-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: var(--card-accent);
  opacity: 0;
  transition: opacity var(--transition-base);
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

.stat-card:hover::before {
  opacity: 1;
}

.stat-top {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
}

.stat-icon-wrap {
  width: 36px;
  height: 36px;
  border-radius: var(--radius-sm);
  background: color-mix(in srgb, var(--card-accent) 10%, transparent);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--card-accent);
}

.stat-label {
  font-size: 13px;
  color: var(--color-text-muted);
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: var(--color-text-primary);
  font-variant-numeric: tabular-nums;
  line-height: 1.1;
}

.stat-sub {
  font-size: 12px;
  color: var(--color-text-light);
  margin-top: 4px;
}

.section-card {
  height: 100%;
}

.section-header {
  display: flex;
  align-items: baseline;
  gap: 8px;
}

.section-title {
  font-size: 15px;
  font-weight: 600;
  color: var(--color-text-primary);
}

.section-desc {
  font-size: 12px;
  color: var(--color-text-light);
}

.quick-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 16px;
}

.quick-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 16px 8px;
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: all var(--transition-base);
}

.quick-item:hover {
  background: var(--color-surface);
  transform: translateY(-2px);
}

.quick-icon {
  width: 44px;
  height: 44px;
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: transform var(--transition-base);
}

.quick-item:hover .quick-icon {
  transform: scale(1.08);
}

.quick-label {
  font-size: 13px;
  color: var(--color-text-secondary);
  font-weight: 500;
}

.about-card :deep(.el-card__body) {
  padding-top: 16px;
}

.about-text {
  font-size: 14px;
  color: var(--color-text-secondary);
  line-height: 1.8;
  margin-bottom: 8px;
}

.about-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-top: 16px;
}

.about-tag {
  font-size: 11px;
  color: var(--color-text-muted);
  background: var(--color-surface);
  padding: 3px 10px;
  border-radius: 20px;
  border: 1px solid var(--color-border-light);
}
</style>