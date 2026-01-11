import Login from '@/components/LoginForm.vue'
import TabConsultation from "@/components/TabConsultation.vue";

import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {path: '/', component: Login},
  {path: '/Consultation', component: TabConsultation}
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

export default router
