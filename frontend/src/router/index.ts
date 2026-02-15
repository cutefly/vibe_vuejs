import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const routes: RouteRecordRaw[] = [
    {
        path: '/login',
        name: 'Login',
        component: () => import('@/pages/LoginPage.vue'),
        meta: { public: true }
    },
    {
        path: '/',
        component: () => import('@/layouts/DefaultLayout.vue'),
        children: [
            {
                path: '',
                name: 'Dashboard',
                component: () => import('@/pages/EmployeeListPage.vue')
            },
            {
                path: 'employees/add',
                name: 'EmployeeAdd',
                component: () => import('@/pages/EmployeeDetailPage.vue')
            },
            {
                path: 'employees/:id/edit',
                name: 'EmployeeEdit',
                component: () => import('@/pages/EmployeeDetailPage.vue'),
                props: true
            }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach((to, _from, next) => {
    const authStore = useAuthStore()

    if (!to.meta.public && !authStore.isAuthenticated) {
        next({ name: 'Login' })
    } else {
        next()
    }
})

export default router
