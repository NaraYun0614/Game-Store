<template>
  <div>
    <input type="text" placeholder="Name" v-model="newCategory.name" />
    <input type="text" placeholder="Description" v-model="newCategory.description" />

    <button @click="createCategory" :disabled="!isCategoryValid()">Create Category</button>
    <button @click="clearInputs">Clear</button>
  </div>
</template>


<script>
import axios from "axios";

const axiosClient = axios.create({
  baseURL: "http://localhost:8080",
});

export default {
  data() {
    return {
      newCategory: {
        name: "",
        description: "",
      },
    };
  },
  methods: {
    async createCategory() {
      try {
        const response = await axiosClient.post("/categories", this.newCategory);
        console.log("Category created:", response.data);
        this.$router.push({ name: "Category" });
      } catch (e) {
        console.error("Error creating category:", e);
      }
    },
    clearInputs() {
      this.newCategory = {
        name: "",
        description: "",
      };
    },
    isCategoryValid() {
      return (
          this.newCategory.name &&
          this.newCategory.description
      );
    },
  },
};
</script>
