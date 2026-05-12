<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">横向项目管理</h2>
      <el-button type="primary" :icon="Plus" @click="handleAdd">新增项目</el-button>
    </div>

    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" inline>
        <el-form-item label="项目名称">
          <el-input v-model="queryForm.name" placeholder="请输入" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="企业名称">
          <el-input v-model="queryForm.companyName" placeholder="请输入" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="全部" clearable style="width:110px">
            <el-option label="在研" value="在研" />
            <el-option label="已结题" value="已结题" />
          </el-select>
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="queryForm.role" placeholder="全部" clearable style="width:110px">
            <el-option label="主持" value="主持" />
            <el-option label="参与" value="参与" />
          </el-select>
        </el-form-item>
        <el-form-item label="签订年份">
          <el-date-picker v-model="queryForm.dateRange" type="yearrange" range-separator="至" start-placeholder="开始年" end-placeholder="结束年" value-format="YYYY" style="width:200px" />
        </el-form-item>
        <el-form-item label="金额(万元)">
          <el-input-number v-model="queryForm.amountMin" :min="0" placeholder="最低" controls-position="right" style="width:105px" />
          <span style="margin:0 4px">-</span>
          <el-input-number v-model="queryForm.amountMax" :min="0" placeholder="最高" controls-position="right" style="width:105px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
          <el-button :icon="RefreshRight" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" style="margin-top:16px">
      <el-table :data="tableData" v-loading="loading" stripe border style="width:100%">
        <el-table-column prop="id" label="ID" width="60" align="center" />
        <el-table-column prop="name" label="项目名称" min-width="180" show-overflow-tooltip />
        <el-table-column prop="companyName" label="合作企业" min-width="160" show-overflow-tooltip />
        <el-table-column prop="contractAmount" label="合同金额(万元)" width="130" align="right" />
        <el-table-column prop="role" label="角色" width="80" align="center" />
        <el-table-column prop="status" label="状态" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === '在研' ? 'success' : 'info'" size="small">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="signDate" label="签订日期" width="110" align="center" />
        <el-table-column prop="endDate" label="完成日期" width="110" align="center" />
        <el-table-column prop="teacherName" label="教师" width="100" align="center" />
        <el-table-column label="操作" width="140" align="center" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link :icon="Edit" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link :icon="Delete" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="queryForm.page"
          v-model:page-size="queryForm.size"
          :page-sizes="[10, 20, 50]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchData"
          @current-change="fetchData"
        />
      </div>
    </el-card>

    <HorizontalProjectFormDialog v-model:visible="dialogVisible" :edit-data="currentRow" @success="fetchData" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Plus, Search, RefreshRight, Edit, Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { pageHorizontalProjects, deleteHorizontalProject } from '../../api/horizontalProject'
import HorizontalProjectFormDialog from './HorizontalProjectFormDialog.vue'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const currentRow = ref(null)

const queryForm = reactive({
  name: '',
  companyName: '',
  status: '',
  role: '',
  dateRange: null,
  amountMin: null,
  amountMax: null,
  page: 1,
  size: 10
})

onMounted(() => {
  fetchData()
})

async function fetchData() {
  loading.value = true
  try {
    const params = { ...queryForm }
    if (params.dateRange && params.dateRange.length === 2) {
      params.signDateBegin = params.dateRange[0] + '-01-01'
      params.signDateEnd = params.dateRange[1] + '-01-01'
    }
    delete params.dateRange
    const res = await pageHorizontalProjects(params)
    tableData.value = res.data.records || []
    total.value = res.data.total || 0
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  queryForm.page = 1
  fetchData()
}

function handleReset() {
  Object.assign(queryForm, {
    name: '',
    companyName: '',
    status: '',
    role: '',
    dateRange: null,
    amountMin: null,
    amountMax: null,
    page: 1,
    size: 10
  })
  fetchData()
}

function handleAdd() {
  currentRow.value = null
  dialogVisible.value = true
}

function handleEdit(row) {
  currentRow.value = { ...row }
  dialogVisible.value = true
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(`确定要删除项目「${row.name}」吗？此操作不可恢复。`, '删除确认', {
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteHorizontalProject(row.id)
    ElMessage.success('删除成功')
    fetchData()
  } catch {
    // cancelled
  }
}
</script>

<style scoped>
.page-container { padding: 0; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.page-title { font-size: 20px; color: #303133; }
.search-card { padding: 4px 0; }
.search-card :deep(.el-card__body) { padding-bottom: 0; }
.pagination-wrap { display: flex; justify-content: flex-end; margin-top: 16px; }
</style>
