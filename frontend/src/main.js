import { createApp } from 'vue';
import App from './App.vue';
import { createRouter, createWebHistory } from 'vue-router';
import MemberRegisterPage from '@/components/MemberRegisterPage.vue';
import MemberInfoPage from '@/components/MemberInfoPage.vue';
import MemberListPage from '@/components/MemberListPage.vue';

const routes = [
  {
    path: '/',
    redirect: '/register'
  },
  {
    path: '/register',
    component: MemberRegisterPage
  },
  {
    path: '/view',
    component: MemberInfoPage
  },
  {
    path: '/list',
    component: MemberListPage
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

createApp(App).use(router).mount('#app');
