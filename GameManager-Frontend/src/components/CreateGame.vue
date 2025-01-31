<template>
  <div class="form-container">
    <h1>Create a New Game</h1>
    <form class="game-form" @submit.prevent="createGame">
      <div class="form-group">
        <label for="title">Title</label>
        <input type="text" id="title" placeholder="Enter game title" v-model="newGame.title" />
      </div>
      <div class="form-group">
        <label for="description">Description</label>
        <input
            type="text"
            id="description"
            placeholder="Enter game description"
            v-model="newGame.description"
        />
      </div>
      <div class="form-group">
        <label for="genre">Genre</label>
        <input type="text" id="genre" placeholder="Enter game genre" v-model="newGame.genre" />
      </div>
      <div class="form-group">
        <label for="price">Price</label>
        <input type="number" id="price" placeholder="Enter game price" v-model="newGame.price" />
      </div>
      <div class="form-group">
        <label for="stock">Stock</label>
        <input type="number" id="stock" placeholder="Enter stock quantity" v-model="newGame.stock" />
      </div>
      <div class="form-group">
        <label for="category">Category</label>
        <select v-model="newGame.categoryId" id="category" required>
          <option value="" disabled>Select a category</option>
          <option
              v-for="category in categories"
              :key="category.categoryId"
              :value="category.categoryId"
          >
            {{ category.name }}
          </option>
        </select>
      </div>
      <div class="form-group">
        <label for="image-url">Image URL</label>
        <input
          type="text"
          id="image-url"
          placeholder="Enter game image URL"
          v-model="newGame.imageUrl"
        />
      </div>
      <div class="button-group">
        <button type="submit" class="btn btn-primary" :disabled="!isGameValid()">Create Game</button>
        <button type="button" class="btn btn-secondary" @click="clearInputs">Clear</button>
      </div>
    </form>
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
      categories: [],
      newGame: {
        title: "",
        description: "",
        genre: "",
        price: 0.0,
        stock: 0,
        gameStatus: "PendingApproval",
        requestStatus: "PendingApproval",
        categoryId: "",
        imageUrl: "",
      },
    };
  },
  created() {
    this.getCategories();
  },
  methods: {
    async createGame() {
      try {
        const response = await axiosClient.post("/api/games", this.newGame);
        console.log("Game created:", response.data);
        this.$router.push({name: "GameList"});
      } catch (e) {
        console.error("Error creating game:", e);
      }
    },
    async getCategories() {
      try {
        const response = await axiosClient.get("/categories");
        if (response.data) {
          this.categories = response.data;
        }
      } catch (e) {
        console.error("Error getting categories:", e);
      }
    },
    clearInputs() {
      this.newGame = {
        title: "",
        description: "",
        genre: "",
        price: 0.0,
        stock: 0,
        gameStatus: "PendingApproval",
        requestStatus: "PendingApproval",
        categoryId: "",
        imageUrl: "",
      };
    },
    isGameValid() {
      return (
          this.newGame.title &&
          this.newGame.description &&
          this.newGame.genre &&
          this.newGame.price > 0 &&
          this.newGame.stock >= 0 &&
          this.newGame.categoryId &&
          this.newGame.imageUrl
      );
    },
  },
};
</script>

<style scoped>
/* General Reset */
body {
  margin: 0;
  padding: 0;
  font-family: Arial, sans-serif;
  background-color: #f4f4f9;
}

/* Form Container */
.form-container {
  max-width: 600px;
  margin: 50px auto;
  background-color: white;
  padding: 20px 30px;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  text-align: center;
}

h1 {
  margin-bottom: 20px;
  font-size: 24px;
  color: #333;
}

/* Form Group */
.form-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 15px;
  text-align: left;
}

label {
  font-size: 14px;
  margin-bottom: 5px;
  font-weight: bold;
  color: #555;
}

input,
select {
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ddd;
  border-radius: 5px;
  width: 100%;
  box-sizing: border-box;
}

input:focus,
select:focus {
  border-color: #82A3A1;
  outline: none;
  box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
}

/* Button Group */
.button-group {
  display: flex;
  justify-content: space-between;
  gap: 10px;
}

button {
  padding: 10px 20px;
  font-size: 16px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.btn-primary {
  background-color: #9FC490;
  color: white;
}

.btn-primary:hover {
  background-color: #0056b3;
}

.btn-primary:disabled {
  background-color: #ddd;
  cursor: not-allowed;
}

.btn-secondary {
  background-color: #9FC490;
  color: white;
}

.btn-secondary:hover {
  background-color: #9FC490;
}

/* Responsive Design */
@media (max-width: 600px) {
  .form-container {
    padding: 15px;
  }

  button {
    width: 100%; /* Buttons stack on smaller screens */
  }

  .button-group {
    flex-direction: column;
    gap: 10px;
  }
}
</style>
