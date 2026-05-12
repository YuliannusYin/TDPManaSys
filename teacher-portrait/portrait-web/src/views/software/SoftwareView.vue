<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">软著管理</h2>
      <el-button type="primary" :icon="Plus" @click="handleAdd">新增软著</el-button>
    </div>

    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" inline>
        <el-form-item label="软件名称">
          <el-input v-model="queryForm.name" placeholder="请输入" clearable style="width:180px" />
        </el-form-item>
        <el-form-item label="登记年份">
          <el-date-picker v-model="queryForm.dateRange" type="yearrange" range-separator="至" start-placeholder="开始年" end-placeholder="结束年" value-format="YYYY" style="width:200px" />
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
        <el-table-column prop="name" label="软件名称" min-width="180" show-overflow-tooltip />
        <el-table-column prop="registrationNo" label="登记号" width="150" show-overflow-tooltip />
        <el-table-column prop="version" label="版本号" width="90" align="center" />
        <el-table-column prop="devCompletionDate" label="开发完成日期" width="120" align="center" />
        <el-table-column prop="firstPublishDate" label="首次发表日期" width="120" align="center" />
        <el-table-column prop="registrationDate" label="登记日期" width="110" align="center" />
        <el-table-column prop="copyrightOwners" label="著作权人" min-width="140" show-overflow-tooltip />
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

    <SoftwareFormDialog v-model:visible="dialogVisible" :edit-data="currentRow" @success="fetchData" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Plus, Search, RefreshRight, Edit, Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { pageSoftwareCopyrights, deleteSoftwareCopyright } from '../../api/softwareCopyright'
import SoftwareFormDialog from './SoftwareFormDialog.vue'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const currentRow = ref(null)

const queryForm = reactive({
  name: '', dateRange: null, page: 1, size: 10
})

onMounted(() => { fetchData() })

async function fetchData() {
  loading.value = true
  try {
    const params = { ...queryForm }
    if (params.dateRange && params.dateRange.length === 2) {
      params.registrationDateBegin = params.dateRange[0] + '-01-01'
      params.registrationDateEnd = params.dateRange[1] + '-01-01'
    }
    delete params.dateRange
    const res = await pageSoftwareCopyrights(params)
    tableData.value = res.data.records || []
    total.value = res.data.total || 0
  } finally { loading.value = false }
}

function handleSearch() { queryForm.page = 1; fetchData() }

function handleReset() {
  Object.assign(queryForm, { name: '', dateRange: null, page: 1, size: 10 })
  fetchData()
}

function handleAdd() { currentRow.value = null; dialogVisible.value = true }
function handleEdit(row) { currentRow.value = { ...row }; dialogVisible.value = true }

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(`确定要删除软著「${row.name}」吗？此操作不可恢复。`, '删除确认', {
      confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
    })
    await deleteSoftwareCopyright(row.id)
    ElMessage.success('删除成功')
    fetchData()
  } catch { /* cancelled */ }
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
