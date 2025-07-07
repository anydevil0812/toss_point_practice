<template>
  <div class="container">
    <h2>회원 조회</h2>

    <div v-if="member" class="result">
      <p><strong>ID:</strong> {{ member.id }}</p>
      <p><strong>이름:</strong> {{ member.name }}</p>
      <p><strong>조회수:</strong> {{ member.views }}</p>
      <p><strong>등록일:</strong> {{ member.registerDate }}</p>
    </div>

    <p class="message">{{ message }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'

// 1. 현재 라우트 객체에서 쿼리 파라미터(memberId) 추출
const route = useRoute()
const memberId = ref(route.query.memberId || '')
const member = ref(null)
const message = ref('')

const getMember = async () => {
  if (!memberId.value) {
    message.value = '회원 ID를 입력하세요.'
    return
  }

  try {
    // 1. 조회수 증가
    await axios.put('/api/member/views', null, {
      params: { memberId: memberId.value }
    })

    // 2. 회원 정보 조회
    const res = await axios.get('/api/member', {
      params: { memberId: memberId.value }
    })

    if (res.data.result) {
      member.value = res.data.result
      message.value = res.data.message
    } else {
      member.value = null
      message.value = res.data.message || '회원을 찾을 수 없습니다.'
    }
  } catch (err) {
    console.error(err)
    message.value = '오류가 발생했습니다.'
  }
}

// 2. 컴포넌트가 mount 되었을 때 실행
onMounted(() => {
  getMember()
})
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
.result {
  margin-top: 1rem;
  background: #f1f1f1;
  padding: 1rem;
}
.message {
  margin-top: 1rem;
  color: #555;
}
</style>
