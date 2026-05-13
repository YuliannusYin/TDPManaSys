<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">用户管理</h2>
      <el-button type="primary" :icon="Plus" @click="handleAdd">新增用户</el-button>
    </div>

    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" inline>
        <el-form-item label="工号">
          <el-input v-model="queryForm.workNo" placeholder="请输入工号" clearable style="width:140px" />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="queryForm.name" placeholder="请输入姓名" clearable style="width:140px" />
        </el-form-item>
        <el-form-item label="学院">
          <el-input v-model="queryForm.college" placeholder="请输入学院" clearable style="width:160px" />
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
        <el-table-column prop="workNo" label="工号" width="100" align="center" />
        <el-table-column prop="name" label="姓名" width="100" align="center" />
        <el-table-column prop="college" label="学院" min-width="140" show-overflow-tooltip />
        <el-table-column prop="role" label="角色" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="row.role === 'ADMIN' ? 'danger' : ''" size="small">{{ row.role === 'ADMIN' ? '管理员' : '教师' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="170" align="center" />
        <el-table-column label="操作" width="240" align="center" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link :icon="Edit" @click="handleEdit(row)">编辑</el-button>
            <el-button type="warning" link :icon="Key" @click="handleResetPwd(row)">重置密码</el-button>
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

    <el-dialog
      :model-value="dialogVisible"
      :title="isEdit ? '编辑用户' : '新增用户'"
      width="500px"
      :close-on-click-modal="false"
      @update:model-value="dialogVisible = $event"
      @closed="handleClose"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="工号" prop="workNo">
          <el-input v-model="form.workNo" :disabled="isEdit" placeholder="请输入工号" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="学院">
          <el-input v-model="form.college" placeholder="请输入学院" />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="form.role" placeholder="请选择角色" style="width:100%">
            <el-option label="教师 (TEACHER)" value="TEACHER" />
            <el-option label="管理员 (ADMIN)" value="ADMIN" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="!isEdit" label="初始密码">
          <el-input v-model="form.password" placeholder="留空则默认为 123456" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Plus, Search, RefreshRight, Edit, Delete, Key } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../../api/request'
import { useUserStore } from '../../store/user'

const userStore = useUserStore()
const currentUserId = userStore.userInfo?.userId

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitting = ref(false)
const formRef = ref(null)

const queryForm = reactive({ workNo: '', name: '', college: '', page: 1, size: 10 })

const form = reactive({ workNo: '', name: '', college: '', role: 'TEACHER', password: '' })

const rules = {
  workNo: [{ required: true, message: '请输入工号', trigger: 'blur' }],
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  role: [{ required: true, message: '请选择角色', trigger: 'change' }]
}

onMounted(() => { fetchData() })

async function fetchData() {
  loading.value = true
  try {
    const params = {}
    if (queryForm.workNo) params.workNo = queryForm.workNo
    if (queryForm.name) params.name = queryForm.name
    if (queryForm.college) params.college = queryForm.college
    params.page = queryForm.page
    params.size = queryForm.size
    const res = await request({ url: '/users', method: 'get', params })
    tableData.value = res.data.records || []
    total.value = res.data.total || 0
  } finally { loading.value = false }
}

function handleSearch() { queryForm.page = 1; fetchData() }
function handleReset() { Object.assign(queryForm, { workNo: '', name: '', college: '', page: 1, size: 10 }); fetchData() }

function handleAdd() {
  isEdit.value = false
  form.workNo = ''; form.name = ''; form.college = ''; form.role = 'TEACHER'; form.password = ''
  dialogVisible.value = true
}

function handleEdit(row) {
  isEdit.value = true
  form.workNo = row.workNo; form.name = row.name; form.college = row.college || ''; form.role = row.role; form.password = ''
  currentRowId.value = row.id
  dialogVisible.value = true
}

const currentRowId = ref(null)

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitting.value = true
  try {
    if (isEdit.value) {
      await request({ url: `/users/${currentRowId.value}`, method: 'put', data: form })
      ElMessage.success('编辑成功')
    } else {
      await request({ url: '/users', method: 'post', data: form })
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    fetchData()
  } finally { submitting.value = false }
}

function handleClose() { formRef.value?.resetFields() }

async function handleDelete(row) {
  if (row.id === currentUserId) {
    ElMessage.warning('不能删除自己')
    return
  }
  try {
    await ElMessageBox.confirm(`确定要删除用户「${row.name}(${row.workNo})」吗？`, '删除确认', {
      confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
    })
    await request({ url: `/users/${row.id}`, method: 'delete' })
    ElMessage.success('删除成功')
    fetchData()
  } catch { /* cancelled */ }
}

async function handleResetPwd(row) {
  try {
    await ElMessageBox.confirm(`确定要将用户「${row.name}」的密码重置为 123456 吗？`, '密码重置', {
      confirmButtonText: '确定重置', cancelButtonText: '取消', type: 'warning'
    })
    await request({ url: `/users/${row.id}/reset-password`, method: 'put' })
    ElMessage.success('密码已重置为 123456')
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
