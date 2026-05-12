<template>
  <el-dialog
    :model-value="visible"
    title="专利转让登记"
    width="480px"
    :close-on-click-modal="false"
    @update:model-value="$emit('update:visible', $event)"
    @closed="handleClose"
  >
    <el-alert v-if="patentData" :title="'操作专利：' + patentData.name" type="info" :closable="false" show-icon style="margin-bottom:16px" />
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="转让日期" prop="transferDate">
        <el-date-picker v-model="form.transferDate" type="date" placeholder="请选择转让日期" value-format="YYYY-MM-DD" style="width:100%" />
      </el-form-item>
      <el-form-item label="受让方名称" prop="transferee">
        <el-input v-model="form.transferee" placeholder="请输入受让方名称" />
      </el-form-item>
      <el-form-item label="转让金额(万元)">
        <el-input-number v-model="form.amount" :min="0" :precision="2" :step="1" placeholder="请输入转让金额" style="width:100%" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="$emit('update:visible', false)">取 消</el-button>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">确认转让</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { transferPatent } from '../../api/patent'

const props = defineProps({ visible: Boolean, patentData: Object })
const emit = defineEmits(['update:visible', 'success'])

const submitting = ref(false)
const formRef = ref(null)

const form = reactive({
  transferDate: '', transferee: '', amount: null
})

const rules = {
  transferDate: [{ required: true, message: '请选择转让日期', trigger: 'change' }],
  transferee: [{ required: true, message: '请输入受让方名称', trigger: 'blur' }]
}

watch(() => props.visible, (val) => {
  if (val) {
    form.transferDate = ''; form.transferee = ''; form.amount = null
  }
})

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitting.value = true
  try {
    await transferPatent(props.patentData.id, form)
    ElMessage.success('专利转让登记成功，该专利已标记为不计入考核')
    emit('success'); emit('update:visible', false)
  } finally { submitting.value = false }
}

function handleClose() {
  formRef.value?.resetFields()
}
</script>
