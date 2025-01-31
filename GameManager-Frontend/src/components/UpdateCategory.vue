<template>
  <div>
    <input type="text" placeholder="Name" v-model="category.name" />
    <input type="text" placeholder="Description" v-model="category.description" />
    <button @click="updateCategory">Update Category</button>
  </div>
</template>

<script>
import axios from "axios";

const axiosClient = axios.create({
  baseURL: "http://localhost:8080",
});

export default {
    props: ['id'], // Accept the ID from the route
    data() {
      return {
        category: {}, // To hold the category data being edited
      };
    },
    async created() {
      try {
        const response = await axios.get(`/api/categories/${this.id}`);
        this.category = response.data;
      } catch (e) {
        console.error("Error fetching category:", e);
      }
    },

  methods: {
    async updateCategory() {
      try {
        const response = await axiosClient.put(`/api/categories/${this.category.categoryId}`, this.category);
        console.log("Category updated:", response.data);
        this.$router.push({ name: "CategoryList" });
      } catch (e) {
        console.error("Error updating category:", e);
      }
    },
  },
};
</script>