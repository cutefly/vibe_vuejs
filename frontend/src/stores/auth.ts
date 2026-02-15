import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export type AdminRole = 'ROLE_READ_ONLY' | 'ROLE_SUPER_ADMIN'

interface User {
    username: string
    name: string
    role: AdminRole
}

export const useAuthStore = defineStore('auth', () => {
    const user = ref<User | null>(JSON.parse(localStorage.getItem('user') || 'null'))
    const token = ref<string | null>(localStorage.getItem('token'))
    const refreshToken = ref<string | null>(localStorage.getItem('refreshToken'))

    const isAuthenticated = computed(() => !!token.value)
    const isSuperAdmin = computed(() => user.value?.role === 'ROLE_SUPER_ADMIN')

    function login(u: User, t: string, rt: string) {
        user.value = u
        token.value = t
        refreshToken.value = rt
        localStorage.setItem('token', t)
        localStorage.setItem('refreshToken', rt)
        localStorage.setItem('user', JSON.stringify(u))
    }

    function updateToken(t: string) {
        token.value = t
        localStorage.setItem('token', t)
    }

    function logout() {
        user.value = null
        token.value = null
        refreshToken.value = null
        localStorage.removeItem('token')
        localStorage.removeItem('refreshToken')
        localStorage.removeItem('user')
    }

    return {
        user,
        token,
        refreshToken,
        isAuthenticated,
        isSuperAdmin,
        login,
        updateToken,
        logout
    }
})
