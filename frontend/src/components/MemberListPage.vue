<template>
  <div class="container">
    <h2>회원 목록</h2>

    <!-- 정렬 옵션 -->
    <div class="sort-controls">
      <label for="sort">정렬 기준:</label>
      <select id="sort" v-model="sortCol" @change="fetchMembers(0)">
        <option value="name">이름 가나다순</option>
        <option value="views">조회순</option>
        <option value="registerDate">등록 최신순</option>
      </select>

      <select v-model="sortOrder" @change="fetchMembers(0)">
        <option value="asc">오름차순</option>
        <option value="desc">내림차순</option>
      </select>
    </div>

    <!-- 회원 목록 테이블 -->
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
          <td>
            <router-link :to="`/info?memberId=${member.id}`">{{ member.name }}</router-link>
          </td>
          <td>{{ member.views }}</td>
          <td>{{ member.registerDate }}</td>
        </tr>
      </tbody>
    </table>

    <!-- 페이지네이션 -->
    <div class="pagination">
      <button @click="goToPage(currentPage - 1)" :disabled="currentPage === 0">이전</button>

      <button
        v-for="page in totalPages"
        :key="page"
        @click="goToPage(page - 1)"
        :class="{ active: currentPage === page - 1 }"
      >
        {{ page }}
      </button>

      <button @click="goToPage(currentPage + 1)" :disabled="currentPage === totalPages - 1">다음</button>
    </div>

    <p class="message">{{ message }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const members = ref([])
const message = ref('')

const currentPage = ref(0)
const totalPages = ref(0)
const pageSize = 5

const sortCol = ref('name')      
const sortOrder = ref('asc')      

const fetchMembers = async (page = 0) => {
  try {
    const res = await axios.get('/api/memberList', {
      params: {
        page: page,
        size: pageSize,
        sortCol: sortCol.value,
        order: sortOrder.value
      }
    })

    const data = res.data.result
    members.value = data.content
    totalPages.value = data.totalPages
    currentPage.value = data.number
    message.value = res.data.message
  } catch (err) {
    console.error(err)
    message.value = '회원 목록 조회 실패'
  }
}

const goToPage = (page) => {
  if (page >= 0 && page < totalPages.value) {
    fetchMembers(page)
  }
}

onMounted(() => {
  fetchMembers()
})
</script>

<style scoped>
.container {
  width: 700px;
  margin: 0 auto;
  text-align: center;
}
.sort-controls {
  margin-bottom: 1rem;
  text-align: right
}
.sort-controls select {
  margin-left: 8px;
  padding: 4px;
}
table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
}
th,
td {
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
.pagination {
  margin-top: 1rem;
}
.pagination button {
  margin: 0 5px;
  padding: 6px 12px;
}
.pagination .active {
  font-weight: bold;
  text-decoration: underline;
}
</style>
