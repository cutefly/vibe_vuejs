<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import axios from '@/utils/axios'
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import Button from 'primevue/button'
import InputText from 'primevue/inputtext'

interface Employee {
  id: number
  name: string
  department: string
  position: string
  email: string
  joinedDate: string
}

const router = useRouter()
const authStore = useAuthStore()

const employees = ref<Employee[]>([])
const loading = ref(true)
const filters = ref({
  global: { value: null, matchMode: 'contains' }
})

const fetchEmployees = async () => {
  loading.value = true
  try {
    const response = await axios.get('/api/employees')
    employees.value = response.data
  } catch (error) {
    console.error('Failed to fetch employees:', error)
  } finally {
    loading.value = false
  }
}

const confirmDelete = async (id: number) => {
  if (confirm('정말로 이 임직원 정보를 삭제하시겠습니까?')) {
    try {
      await axios.delete(`/api/employees/${id}`)
      employees.value = employees.value.filter(e => e.id !== id)
    } catch (error) {
      console.error('Failed to delete employee:', error)
      alert('삭제에 실패하였습니다.')
    }
  }
}

onMounted(() => {
  fetchEmployees()
})
</script>

<template>
  <div class="page-container">
    <div class="header">
      <h1>임직원 정보 조회</h1>
      <Button 
        v-if="authStore.isSuperAdmin" 
        label="임직원 추가" 
        icon="pi pi-plus" 
        @click="router.push('/employees/add')" 
      />
    </div>

    <DataTable 
      :value="employees" 
      :loading="loading" 
      paginator 
      :rows="10" 
      dataKey="id"
      :globalFilterFields="['name', 'department', 'position', 'email']"
      v-model:filters="filters"
    >
      <template #header>
        <div class="flex justify-content-end">
          <span class="p-input-icon-left">
            <i class="pi pi-search" />
            <InputText v-model="filters['global'].value" placeholder="검색..." />
          </span>
        </div>
      </template>

      <Column field="id" header="ID" sortable></Column>
      <Column field="name" header="이름" sortable></Column>
      <Column field="department" header="부서" sortable></Column>
      <Column field="position" header="직책" sortable></Column>
      <Column field="email" header="이메일" sortable></Column>
      <Column field="joinedDate" header="입사일" sortable></Column>
      
      <Column header="Actions" v-if="authStore.isSuperAdmin">
        <template #body="slotProps">
          <div class="actions">
            <Button 
              icon="pi pi-pencil" 
              class="p-button-text p-button-sm" 
              @click="router.push(`/employees/${slotProps.data.id}/edit`)" 
            />
            <Button 
              icon="pi pi-trash" 
              class="p-button-text p-button-danger p-button-sm" 
              @click="confirmDelete(slotProps.data.id)" 
            />
          </div>
        </template>
      </Column>
    </DataTable>
  </div>
</template>

<style scoped>
.page-container {
  background-color: #fff;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

h1 {
  margin: 0;
  font-size: 1.5rem;
  color: var(--text-color);
}

.actions {
  display: flex;
  gap: 0.5rem;
}

.flex {
  display: flex;
}

.justify-content-end {
  justify-content: flex-end;
}
</style>
