import { createApp } from 'vue';
import App from './App.vue';
import { createRouter, createWebHistory } from 'vue-router';
import MemberRegisterPage from '@/components/MemberRegisterPage.vue';
import MemberInfoPage from '@/components/MemberInfoPage.vue';
import MemberListPage from '@/components/MemberListPage.vue';

const routes = [
  {
    path: '/',
    redirect: '/list'
  },
  {
    path: '/register',
    component: MemberRegisterPage
  },
  {
    path: '/info',
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
