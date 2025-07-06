<template>
  <div id="app">
    <img alt="Vue logo" src="./assets/logo.png" class="logo" />

    <!-- 네비게이션 -->
    <nav class="nav">
      <router-link to="/info">회원 조회</router-link>
      <router-link to="/register">회원 등록</router-link>
      <router-link to="/list">회원 목록</router-link>
    </nav>

    <!-- 페이지 영역 -->
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  text-align: center;
  color: #2c3e50;
  margin-top: 40px;
}
.logo {
  width: 120px;
  margin-bottom: 20px;
}
.nav {
  margin-bottom: 30px;
}
.nav a {
  margin: 0 15px;
  color: #42b983;
  text-decoration: none;
  font-weight: bold;
}
.nav a.router-link-active {
  text-decoration: underline;
}
</style>
