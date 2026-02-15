<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import axios from '@/utils/axios'
import InputText from 'primevue/inputtext'
import Button from 'primevue/button'
import Card from 'primevue/card'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const isEdit = computed(() => !!route.params.id)
const id = route.params.id as string

const employee = ref({
  name: '',
  department: '',
  position: '',
  email: '',
  joinedDate: ''
})

const loading = ref(false)

const fetchEmployee = async () => {
    loading.value = true
    try {
        const response = await axios.get(`/api/employees/${id}`)
        employee.value = response.data
    } catch (error) {
        console.error('Failed to fetch employee:', error)
        alert('정보를 불러오는데 실패하였습니다.')
        router.push('/')
    } finally {
        loading.value = false
    }
}

onMounted(() => {
  if (!authStore.isSuperAdmin) {
    router.push('/')
    return
  }

  if (isEdit.value) {
    fetchEmployee()
  }
})

const handleSubmit = async () => {
    loading.value = true
    try {
        if (isEdit.value) {
            await axios.put(`/api/employees/${id}`, employee.value)
            alert('수정되었습니다.')
        } else {
            await axios.post('/api/employees', employee.value)
            alert('추가되었습니다.')
        }
        router.push('/')
    } catch (error) {
        console.error('Failed to save employee:', error)
        alert('저장에 실패하였습니다.')
    } finally {
        loading.value = false
    }
}
</script>

<template>
  <div class="page-container">
    <Card class="form-card">
      <template #title>
        <h1>{{ isEdit ? '임직원 정보 수정' : '임직원 정보 추가' }}</h1>
      </template>
      <template #content>
        <form @submit.prevent="handleSubmit" class="p-fluid">
          <div class="field">
            <label for="name">이름</label>
            <InputText id="name" v-model="employee.name" required />
          </div>
          <div class="field">
            <label for="department">부서</label>
            <InputText id="department" v-model="employee.department" required />
          </div>
          <div class="field">
            <label for="position">직책</label>
            <InputText id="position" v-model="employee.position" required />
          </div>
          <div class="field">
            <label for="email">이메일</label>
            <InputText id="email" v-model="employee.email" type="email" required />
          </div>
          <div class="field">
            <label for="joinedDate">입사일</label>
            <InputText id="joinedDate" v-model="employee.joinedDate" type="date" required />
          </div>

          <div class="form-actions mt-4">
            <Button type="button" label="취소" class="p-button-secondary p-button-text" @click="router.push('/')" />
            <Button type="submit" :label="isEdit ? '수정' : '추가'" :loading="loading" />
          </div>
        </form>
      </template>
    </Card>
  </div>
</template>

<style scoped>
.page-container {
  display: flex;
  justify-content: center;
}

.form-card {
  width: 100%;
  max-width: 600px;
}

.field {
  margin-bottom: 1.5rem;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
}

.mt-4 {
  margin-top: 2rem;
}

h1 {
  font-size: 1.5rem;
  margin: 0;
}
</style>
