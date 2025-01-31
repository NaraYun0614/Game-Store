<template>
  <div>
    <h1>Manage Games</h1>
    <div>
      <h2>Add New Game</h2>
      <form @submit.prevent="addGame">
        <input v-model="newGame.title" placeholder="Title" required />
        <input v-model="newGame.description" placeholder="Description" required />
        <input v-model="newGame.genre" placeholder="Genre" required />
        <input v-model.number="newGame.price" placeholder="Price" required type="number" />
        <input v-model.number="newGame.stock" placeholder="Stock" required type="number" />
        <button type="submit">Add Game</button>
      </form>
    </div>

    <div>
      <h2>Game List</h2>
      <table>
        <thead>
        <tr>
          <th>Title</th>
          <th>Description</th>
          <th>Genre</th>
          <th>Price</th>
          <th>Stock</th>
          <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="game in games" :key="game.id">
          <td>{{ game.title }}</td>
          <td>{{ game.description }}</td>
          <td>{{ game.genre }}</td>
          <td>{{ game.price }}</td>
          <td>{{ game.stock }}</td>
          <td>
            <button @click="deleteGame(game.id)">Delete</button>
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
  baseURL: "http://localhost:8080/api/games", // Updated base URL to match backend API
});

export default {
  data() {
    return {
      newGame: {
        title: "",
        description: "",
        genre: "",
        price: 0,
        stock: 0,
      },
      games: [],
    };
  },
  async created() {
    this.fetchGames();
  },
  methods: {
    async fetchGames() {
      try {
        const response = await axiosClient.get("/");
        this.games = response.data;
      } catch (error) {
        console.error("Error fetching games:", error);
        alert("Failed to fetch games.");
      }
    },
    async addGame() {
      try {
        const response = await axiosClient.post("/direct-add", this.newGame);
        this.games.push(response.data);
        this.newGame = { title: "", description: "", genre: "", price: 0, stock: 0 };
        alert("Game added successfully!");
      } catch (error) {
        console.error("Error adding game:", error);
        alert("Failed to add game.");
      }
    },
    async deleteGame(gameId) {
      try {
        await axiosClient.delete(`/direct-remove/${gameId}`);
        this.games = this.games.filter((game) => game.id !== gameId);
        alert("Game deleted successfully!");
      } catch (error) {
        console.error("Error deleting game:", error);
        alert("Failed to delete game.");
      }
    },
  },
};
</script>
