<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">论文管理</h2>
      <el-button type="primary" :icon="Plus" @click="handleAdd">新增论文</el-button>
    </div>

    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" inline>
        <el-form-item label="论文题目">
          <el-input v-model="queryForm.title" placeholder="请输入" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="论文类型">
          <el-select v-model="queryForm.type" placeholder="全部" clearable style="width:120px">
            <el-option label="期刊论文" value="期刊论文" />
            <el-option label="会议论文" value="会议论文" />
          </el-select>
        </el-form-item>
        <el-form-item label="期刊/会议">
          <el-input v-model="queryForm.journalName" placeholder="请输入" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="作者排序">
          <el-select v-model="queryForm.authorOrder" placeholder="全部" clearable style="width:100px">
            <el-option label="第1作者" :value="1" />
            <el-option label="第2作者" :value="2" />
            <el-option label="第3作者" :value="3" />
            <el-option label="第4+作者" :value="4" />
          </el-select>
        </el-form-item>
      </el-form>
      <el-form :model="queryForm" inline style="margin-top:4px">
        <el-form-item label="收录标签">
          <el-select v-model="queryForm.indexTypes" multiple placeholder="全部" clearable collapse-tags collapse-tags-tooltip style="width:280px">
            <el-option label="SCI" value="SCI" />
            <el-option label="SSCI" value="SSCI" />
            <el-option label="EI" value="EI" />
            <el-option label="CSCD" value="CSCD" />
            <el-option label="CSSCI" value="CSSCI" />
            <el-option label="北大核心" value="北大核心" />
            <el-option label="普通期刊" value="普通期刊" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="发表年份">
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
        <el-table-column prop="title" label="论文题目" min-width="220" show-overflow-tooltip />
        <el-table-column prop="type" label="类型" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="row.type === '期刊论文' ? '' : 'success'" size="small">{{ row.type }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="journalName" label="期刊/会议" min-width="160" show-overflow-tooltip />
        <el-table-column prop="publishDate" label="发表时间" width="110" align="center" />
        <el-table-column prop="authors" label="作者" min-width="120" show-overflow-tooltip />
        <el-table-column prop="authorOrder" label="排序" width="70" align="center" />
        <el-table-column label="收录标签" min-width="200">
          <template #default="{ row }">
            <el-tag v-for="tag in row.indexTypes" :key="tag" :type="indexColor(tag)" size="small" style="margin:2px">{{ tag }}</el-tag>
          </template>
        </el-table-column>
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

    <PaperFormDialog v-model:visible="dialogVisible" :edit-data="currentRow" @success="fetchData" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Plus, Search, RefreshRight, Edit, Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { pagePapers, deletePaper } from '../../api/paper'
import PaperFormDialog from './PaperFormDialog.vue'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const currentRow = ref(null)

const queryForm = reactive({
  title: '', type: '', journalName: '', authorOrder: null,
  indexTypes: [], dateRange: null, page: 1, size: 10
})

onMounted(() => { fetchData() })

async function fetchData() {
  loading.value = true
  try {
    const params = { ...queryForm }
    if (params.indexTypes && params.indexTypes.length > 0) {
      params.indexTypes = params.indexTypes.join(',')
    } else {
      params.indexTypes = ''
    }
    if (params.dateRange && params.dateRange.length === 2) {
      params.publishDateBegin = params.dateRange[0] + '-01-01'
      params.publishDateEnd = params.dateRange[1] + '-01-01'
    }
    delete params.dateRange
    const res = await pagePapers(params)
    tableData.value = res.data.records || []
    total.value = res.data.total || 0
  } finally { loading.value = false }
}

function handleSearch() { queryForm.page = 1; fetchData() }

function handleReset() {
  Object.assign(queryForm, {
    title: '', type: '', journalName: '', authorOrder: null,
    indexTypes: [], dateRange: null, page: 1, size: 10
  })
  fetchData()
}

function handleAdd() { currentRow.value = null; dialogVisible.value = true }
function handleEdit(row) { currentRow.value = { ...row }; dialogVisible.value = true }

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(`确定要删除论文「${row.title}」吗？收录标签将同步删除。`, '删除确认', {
      confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
    })
    await deletePaper(row.id)
    ElMessage.success('删除成功')
    fetchData()
  } catch { /* cancelled */ }
}

function indexColor(type) {
  const map = { 'SCI': 'danger', 'SSCI': 'danger', 'EI': 'warning', 'CSCD': '', 'CSSCI': 'success', '北大核心': '', '普通期刊': 'info', '其他': 'info' }
  return map[type] || 'info'
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
