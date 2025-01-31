<template>
  <div class="category-list-container">
    <div class="header">
      <h1>Category List</h1>
      <p>View and manage all categories with details and actions.</p>
    </div>

    <!-- Table to display categories -->
    <table class="category-table">
      <thead>
      <tr>
        <th>ID</th>
        <th>NAME</th>
        <th>DESCRIPTION</th>
        <th>ACTIONS</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="category in categories" :key="category.categoryId">
        <td>{{ category.categoryId }}</td>
        <td>{{ category.name }}</td>
        <td>{{ category.description }}</td>
        <td class="action-buttons">
          <button class="btn btn-edit" @click="editCategory(category.categoryId)">Edit</button>
          <button class="btn btn-delete" @click="deleteCategory(category.categoryId)">Delete</button>
        </td>
      </tr>
      </tbody>
    </table>

    <!-- Button to navigate to create category -->
    <div class="create-category">
      <button class="btn btn-create" @click="createCategory">Create New Category</button>
    </div>
  </div>
</template>

<script>
import axios from "axios";

const axiosClient = axios.create({
  baseURL: "http://localhost:8080", // Backend API base URL
});

export default {
  name: "CategoryList",
  data() {
    return {
      categories: [], // Array to store categories fetched from backend
    };
  },

  async created() {
    try {
      // Fetch categories
      const response = await axiosClient.get("/categories");
      this.categories = response.data;
    } catch (e) {
      console.error("Error fetching categories:", e); // Log any errors
    }
  },
  methods: {
    createCategory() {
      this.$router.push({ name: "CreateCategory" });
    },
    editCategory(categoryId) {
      this.$router.push({ name: "UpdateCategory", params: { id: categoryId } });
    },
    async deleteCategory(categoryId) {
      if (!confirm("Are you sure you want to delete this category?")) {
        return;
      }
      try {
        await axiosClient.delete(`/categories/${categoryId}`);
        this.categories = this.categories.filter((category) => category.categoryId !== categoryId);
        alert("Category deleted successfully!");
      } catch (error) {
        console.error("Error deleting category:", error);
        alert("Failed to delete category.");
      }
    },
  },
};
</script>

<style scoped>
/* General Container */
.category-list-container {
  max-width: 1200px;
  margin: 40px auto;
  padding: 20px;
  background-color: #ffffff;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

/* Header */
.header {
  text-align: left;
  margin-bottom: 20px;
}

.header h1 {
  font-size: 24px;
  margin-bottom: 5px;
  color: #333;
}

.header p {
  font-size: 16px;
  color: #555;
}

/* Table Styling */
.category-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
  text-align: center;
}

.category-table thead {
  border-bottom: 2px solid #e0e0e0; /* Subtle underline */
}

.category-table th {
  font-weight: bold;
  text-transform: uppercase;
  color: #6c757d; /* Light gray color */
  font-size: 14px;
  padding: 12px 8px;
  letter-spacing: 0.5px;
}

.category-table td {
  padding: 12px 8px;
  color: #333;
}

.category-table tbody tr:nth-child(even) {
  background-color: #f9f9f9;
}

.category-table tbody tr:hover {
  background-color: #f4f4f4;
}

/* Action Buttons */
.action-buttons {
  display: flex;
  gap: 10px;
}

button {
  padding: 8px 16px;
  font-size: 14px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: all 0.3s ease;
}

button:hover {
  transform: translateY(-2px);
}

/* Button Variants */
.btn {
  color: white;
  font-weight: bold;
}

.btn-create {
  background-color: #007bff;
}

.btn-create:hover {
  background-color: #0056b3;
}

.btn-edit {
  background-color: #C0DFA1;
}

.btn-edit:hover {
  background-color: #218838;
}

.btn-delete {
  background-color: #c82333;
}

.btn-delete:hover {
  background-color: #c82333;
}

/* Responsive Design */
@media (max-width: 768px) {
  .category-list-container {
    padding: 10px;
  }

  .category-table th,
  .category-table td {
    padding: 8px;
    font-size: 14px;
  }

  button {
    font-size: 12px;
    padding: 6px 12px;
  }
}
</style>
