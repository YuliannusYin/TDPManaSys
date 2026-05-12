<template>
  <el-dialog
    :model-value="visible"
    title="专利转让"
    width="480px"
    :close-on-click-modal="false"
    @update:model-value="$emit('update:visible', $event)"
    @closed="handleClose"
  >
    <el-alert type="info" :closable="false" style="margin-bottom:16px">
      <template #title>正在转让专利：{{ patentName }}</template>
    </el-alert>
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="转让日期" prop="transferDate">
        <el-date-picker v-model="form.transferDate" type="date" placeholder="请选择转让日期" value-format="YYYY-MM-DD" style="width:100%" />
      </el-form-item>
      <el-form-item label="受让方名称" prop="transferee">
        <el-input v-model="form.transferee" placeholder="请输入受让方名称" />
      </el-form-item>
      <el-form-item label="转让金额(万元)">
        <el-input-number v-model="form.amount" :min="0" :precision="2" placeholder="请输入转让金额" style="width:100%" />
      </el-form-item>
      <el-form-item label="是否计入考核">
        <el-radio-group v-model="form.isCounted">
          <el-radio :label="1">是</el-radio>
          <el-radio :label="0">否</el-radio>
        </el-radio-group>
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

const props = defineProps({ visible: Boolean, patentId: Number, patentName: String })
const emit = defineEmits(['update:visible', 'success'])

const submitting = ref(false)
const formRef = ref(null)

const form = reactive({
  transferDate: '',
  transferee: '',
  amount: null,
  isCounted: 1
})

const rules = {
  transferDate: [{ required: true, message: '请选择转让日期', trigger: 'change' }],
  transferee: [{ required: true, message: '请输入受让方名称', trigger: 'blur' }]
}

watch(() => props.visible, (val) => {
  if (val) {
    form.transferDate = ''
    form.transferee = ''
    form.amount = null
    form.isCounted = 1
  }
})

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitting.value = true
  try {
    await transferPatent(props.patentId, form)
    ElMessage.success('转让成功')
    emit('success')
    emit('update:visible', false)
  } finally { submitting.value = false }
}

function handleClose() {
  formRef.value?.resetFields()
}
</script>
