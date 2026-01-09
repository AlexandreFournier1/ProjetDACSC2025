import Login from '@/components/LoginForm.vue'
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {path: '/', component: Login}
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

export default router
