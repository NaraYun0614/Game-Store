<template>
  <div>
    <h1>Manage Categories</h1>
    <div>
      <h2>Add New Category</h2>
      <form @submit.prevent="addCategory">
        <input v-model="newCategory.name" placeholder="Name" required />
        <input v-model="newCategory.description" placeholder="Description" required />
        <button type="submit">Add Category</button>
      </form>
    </div>

    <div>
      <h2>Category List</h2>
      <table>
        <thead>
        <tr>
          <th>Name</th>
          <th>Description</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="category in categories" :key="category.id">
          <td>{{ category.name }}</td>
          <td>{{ category.description }}</td>
          <td>
            <button @click="deleteCategory(category.id)">Delete</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import axios from "axios";

const axiosClient = axios.create({
  baseURL: "http://localhost:8080/api/categories", // Updated base URL to match backend API
});

export default {
  data() {
    return {
      newCategory: {
        name: "",
        description: "",
      },
      categories: [],
    };
  },
  async created() {
    this.fetchCategories();
  },
  methods: {
    async fetchCategories() {
      try {
        const response = await axiosClient.get("/category-list");
        this.categories = response.data;
      } catch (error) {
        console.error("Error fetching categories:", error);
        alert("Failed to fetch categories.");
      }
    },
    async addCategory() {
      try {
        const response = await axiosClient.post("/direct-add", this.newCategory);
        this.categories.push(response.data);
        this.newCategory = { name: "", description: "" };
        alert("Category added successfully!");
      } catch (error) {
        console.error("Error adding category:", error);
        alert("Failed to add category.");
      }
    },
    async deleteCategory(categoryId) {
      try {
        await axiosClient.delete(`/direct-remove/${categoryId}`);
        this.categories = this.categories.filter((category) => category.id !== categoryId);
        alert("Category deleted successfully!");
      } catch (error) {
        console.error("Error deleting category:", error);
        alert("Failed to delete category.");
      }
    },
  },
};
</script>