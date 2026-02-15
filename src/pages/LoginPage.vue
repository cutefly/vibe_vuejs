<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import axios from '@/utils/axios'
import InputText from 'primevue/inputtext'
import Password from 'primevue/password'
import Button from 'primevue/button'
import Card from 'primevue/card'

const router = useRouter()
const authStore = useAuthStore()

const username = ref('')
const password = ref('')
const loading = ref(false)
const errorMessage = ref('')

const handleLogin = async () => {
    loading.value = true
    errorMessage.value = ''
    
    try {
        // Authenticate with backend using POST
        const response = await axios.post('/api/auth/login', {
            username: username.value,
            password: password.value
        })
        
        // Save to store (using tokens returned from backend)
        authStore.login(response.data, response.data.token, response.data.refreshToken)
        
        router.push('/')
    } catch (error: any) {
        console.error('Login failed:', error)
        errorMessage.value = '아이디 또는 비밀번호가 올바르지 않습니다.'
    } finally {
        loading.value = false
    }
}
</script>

<template>
  <div class="login-container">
    <Card class="login-card">
      <template #title>
        <h2 class="text-center">VIBE KYE Login</h2>
      </template>
      <template #content>
        <form @submit.prevent="handleLogin" class="p-fluid">
          <div class="field">
            <label for="username">Username</label>
            <InputText id="username" v-model="username" required />
          </div>
          <div class="field">
            <label for="password">Password</label>
            <Password id="password" v-model="password" :feedback="false" toggleMask required />
          </div>
          <p v-if="errorMessage" class="error-text">{{ errorMessage }}</p>
          <Button type="submit" label="Login" :loading="loading" class="mt-4" />
        </form>
        <div class="hints mt-4">
          <p><small>Hint: use 'admin' / 'admin123' or 'user' / 'user123'</small></p>
        </div>
      </template>
    </Card>
  </div>
</template>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: var(--background-color);
}

.login-card {
  width: 100%;
  max-width: 400px;
}

.text-center {
  text-align: center;
}

.field {
  margin-bottom: 1rem;
}

.mt-4 {
  margin-top: 1rem;
}

.hints {
  text-align: center;
  color: var(--secondary-color);
}

.error-text {
    color: var(--error-color);
    font-size: 0.875rem;
    margin-top: 0.5rem;
}
</style>
