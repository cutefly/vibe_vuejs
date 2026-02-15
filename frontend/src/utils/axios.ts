import axios from 'axios'

const instance = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
})

instance.interceptors.request.use((config) => {
    const token = localStorage.getItem('token')
    if (token) {
        config.headers.Authorization = `Bearer ${token}`
    }
    return config
})

instance.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config

        if (error.response?.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true
            const refreshToken = localStorage.getItem('refreshToken')

            if (refreshToken) {
                try {
                    const response = await axios.post(`${import.meta.env.VITE_API_BASE_URL}/api/auth/refreshToken`, {
                        refreshToken: refreshToken
                    })

                    const newToken = response.data.accessToken
                    localStorage.setItem('token', newToken)

                    // Update original request with new token
                    originalRequest.headers.Authorization = `Bearer ${newToken}`
                    return instance(originalRequest)
                } catch (refreshError) {
                    // Refresh token failed, logout user
                    localStorage.removeItem('token')
                    localStorage.removeItem('refreshToken')
                    localStorage.removeItem('user')
                    window.location.href = '/login'
                    return Promise.reject(refreshError)
                }
            }
        }

        return Promise.reject(error)
    }
)

export default instance
