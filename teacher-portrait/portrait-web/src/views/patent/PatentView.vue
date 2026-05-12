<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">专利管理</h2>
      <el-button type="primary" :icon="Plus" @click="handleAdd">新增专利</el-button>
    </div>

    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" inline>
        <el-form-item label="专利名称">
          <el-input v-model="queryForm.name" placeholder="请输入" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="专利类型">
          <el-select v-model="queryForm.type" placeholder="全部" clearable style="width:120px">
            <el-option label="发明专利" value="发明专利" />
            <el-option label="实用新型" value="实用新型" />
            <el-option label="外观设计" value="外观设计" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="全部" clearable style="width:110px">
            <el-option label="申请中" value="申请中" />
            <el-option label="已授权" value="已授权" />
          </el-select>
        </el-form-item>
        <el-form-item label="申请年份">
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
        <el-table-column prop="name" label="专利名称" min-width="180" show-overflow-tooltip />
        <el-table-column prop="type" label="专利类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="typeColor(row.type)" size="small">{{ row.type }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="statusColor(row.status)" size="small">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="applicationNo" label="申请号" width="140" show-overflow-tooltip />
        <el-table-column prop="grantNo" label="授权号" width="140" show-overflow-tooltip />
        <el-table-column prop="applicationDate" label="申请日期" width="110" align="center" />
        <el-table-column prop="grantDate" label="授权日期" width="110" align="center" />
        <el-table-column prop="inventors" label="发明人" min-width="120" show-overflow-tooltip />
        <el-table-column prop="teacherName" label="教师" width="100" align="center" />
        <el-table-column prop="isCounted" label="考核" width="70" align="center">
          <template #default="{ row }">
            <el-tag :type="row.isCounted ? 'success' : 'info'" size="small">{{ row.isCounted ? '是' : '否' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link :icon="Edit" @click="handleEdit(row)">编辑</el-button>
            <el-button v-if="row.status === '已授权'" type="warning" link :icon="Sell" @click="handleTransfer(row)">转让</el-button>
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

    <PatentFormDialog v-model:visible="dialogVisible" :edit-data="currentRow" @success="fetchData" />
    <TransferDialog v-model:visible="transferVisible" :patent-id="transferPatentId" :patent-name="transferPatentName" @success="fetchData" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Plus, Search, RefreshRight, Edit, Delete, Sell } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { pagePatents, deletePatent } from '../../api/patent'
import PatentFormDialog from './PatentFormDialog.vue'
import TransferDialog from './TransferDialog.vue'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const currentRow = ref(null)
const transferVisible = ref(false)
const transferPatentId = ref(null)
const transferPatentName = ref('')

const queryForm = reactive({
  name: '', type: '', status: '', dateRange: null, page: 1, size: 10
})

onMounted(() => { fetchData() })

async function fetchData() {
  loading.value = true
  try {
    const params = { ...queryForm }
    if (params.dateRange && params.dateRange.length === 2) {
      params.applicationDateBegin = params.dateRange[0] + '-01-01'
      params.applicationDateEnd = params.dateRange[1] + '-01-01'
    }
    delete params.dateRange
    const res = await pagePatents(params)
    tableData.value = res.data.records || []
    total.value = res.data.total || 0
  } finally { loading.value = false }
}

function handleSearch() { queryForm.page = 1; fetchData() }

function handleReset() {
  Object.assign(queryForm, { name: '', type: '', status: '', dateRange: null, page: 1, size: 10 })
  fetchData()
}

function handleAdd() { currentRow.value = null; dialogVisible.value = true }
function handleEdit(row) { currentRow.value = { ...row }; dialogVisible.value = true }
function handleTransfer(row) { transferPatentId.value = row.id; transferPatentName.value = row.name; transferVisible.value = true }

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(`确定要删除专利「${row.name}」吗？转让记录将同步删除。`, '删除确认', {
      confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
    })
    await deletePatent(row.id)
    ElMessage.success('删除成功')
    fetchData()
  } catch { /* cancelled */ }
}

function typeColor(type) {
  return { '发明专利': 'danger', '实用新型': 'warning', '外观设计': 'info' }[type] || 'info'
}

function statusColor(status) {
  return status === '已授权' ? 'success' : ''
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
