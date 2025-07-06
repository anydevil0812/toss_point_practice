<template>
  <div class="member-view">
    <h1 class="text-2xl font-bold mb-4">회원 정보 조회</h1>

    <!-- 검색 폼 -->
    <div class="mb-4">
      <input
        v-model="memberId"
        type="number"
        placeholder="회원 ID 입력"
        class="border p-2 rounded mr-2"
      />
      <button @click="fetchMember" class="bg-blue-500 text-white px-4 py-2 rounded">
        조회
      </button>
    </div>

    <!-- 메시지 출력 -->
    <div v-if="message" class="mb-2 text-sm text-gray-600">
      {{ message }}
    </div>

    <!-- 회원 정보 출력 -->
    <div v-if="member" class="p-4 border rounded shadow bg-white">
      <p><strong>ID:</strong> {{ member.id }}</p>
      <p><strong>이름:</strong> {{ member.name }}</p>
      <p><strong>조회수:</strong> {{ member.views }}</p>
      <p><strong>등록일:</strong> {{ member.registerDate }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

// 상태 변수
const memberId = ref('')
const member = ref(null)
const message = ref('')

// 회원 정보 조회 함수
const fetchMember = async () => {
  message.value = ''
  member.value = null

  if (!memberId.value) {
    message.value = '회원 ID를 입력해주세요.'
    return
  }

  try {
    const res = await axios.get(`/member`, {
      params: { memberId: memberId.value }
    })

    const response = res.data
    message.value = response.message

    if (response.status === 'OK' && response.data) {
      member.value = response.data
    }
  } catch (err) {
    console.error(err)
    message.value = '서버 오류로 인해 회원 정보를 불러올 수 없습니다.'
  }
}
</script>

<style scoped>
.member-view {
  max-width: 500px;
  margin: 0 auto;
  padding: 1rem;
}
</style>
