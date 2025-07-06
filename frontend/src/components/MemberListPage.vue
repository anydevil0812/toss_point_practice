<template>
  <div class="container">
    <h2>회원 목록</h2>
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>이름</th>
          <th>조회수</th>
          <th>등록일</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="member in members" :key="member.id">
          <td>{{ member.id }}</td>
          <td>{{ member.name }}</td>
          <td>{{ member.views }}</td>
          <td>{{ member.registerDate }}</td>
        </tr>
      </tbody>
    </table>

    <p class="message">{{ message }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const members = ref([])
const message = ref('')

const fetchMembers = async () => {
  try {
    const res = await axios.get('/members')
    members.value = res.data.result || []
    message.value = res.data.message
  } catch (err) {
    console.error(err)
    message.value = '회원 목록 조회 실패'
  }
}

onMounted(() => {
  fetchMembers()
})
</script>

<style scoped>
.container {
  width: 600px;
  margin: 0 auto;
  text-align: center;
}
table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
}
th, td {
  border: 1px solid #ccc;
  padding: 8px;
}
th {
  background-color: #eee;
}
.message {
  margin-top: 1rem;
  color: #555;
}
</style>
