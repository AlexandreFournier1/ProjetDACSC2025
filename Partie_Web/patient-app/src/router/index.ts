import Login from '@/components/LoginForm.vue'
import TabConsultation from "@/components/TabConsultation.vue";
import ReservationConsultation from '@/components/ReservationConsultation.vue';

import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {path: '/', component: Login},
  {path: '/consultation', component: TabConsultation},
  {path: '/reservation', component: ReservationConsultation},

]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

export default router
