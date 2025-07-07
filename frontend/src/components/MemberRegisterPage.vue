<template>
  <div class="container">
    <h2>회원 등록</h2>
    <div class="form-group">
      <label>이름</label>
      <input v-model="name" type="text" />
    </div>
    <button @click="registerMember">등록하기</button>
    <p class="message">{{ message }}</p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const name = ref('')
const message = ref('')
const router = useRouter()

const registerMember = async () => {
  if (!name.value) {
    message.value = '이름을 입력해주세요.'
    return
  }

  try {
    const res = await axios.post('/api/member', { name: name.value })
    message.value = res.data.message

    // 등록 성공 시 알림 → 목록 페이지로 이동
    if (res.data.message.includes('회원 등록이 정상적으로 완료되었습니다.')) {
      alert(res.data.message)
      router.push('/list')
    }
  } catch (err) {
    console.error(err)
    message.value = '회원 등록 중 오류가 발생했습니다.'
  }
}
</script>

<style scoped>
.container {
  width: 400px;
  margin: 0 auto;
  text-align: left;
}
.form-group {
  margin-bottom: 1rem;
}
input {
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
}
button {
  padding: 8px 16px;
  background-color: #2c3e50;
  color: white;
  border: none;
  cursor: pointer;
}
.message {
  margin-top: 1rem;
  color: #555;
}
</style>
