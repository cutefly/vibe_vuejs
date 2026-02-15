<script setup lang="ts">
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import Button from 'primevue/button'

const router = useRouter()
const authStore = useAuthStore()

const handleLogout = () => {
  authStore.logout()
  router.push({ name: 'Login' })
}
</script>

<template>
  <div class="layout-wrapper">
    <header class="top-bar">
      <div class="logo">VIBE KYE</div>
      <div class="user-info">
        <span class="username">{{ authStore.user?.username }} ({{ authStore.user?.role }})</span>
        <Button label="Logout" icon="pi pi-sign-out" class="p-button-text p-button-sm" @click="handleLogout" />
      </div>
    </header>
    <main class="content">
      <router-view />
    </main>
  </div>
</template>

<style scoped>
.layout-wrapper {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 2rem;
  height: 60px;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.logo {
  font-size: 1.5rem;
  font-weight: bold;
  color: var(--primary-color);
}

.user-info {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.username {
  font-weight: 500;
}

.content {
  flex: 1;
  padding: 2rem;
}
</style>
