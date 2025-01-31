<template>
  <div class="approve-games">
    <h1>Approve Game Additions</h1>
    <div v-if="pendingGames.length === 0">
      <p>No pending games at the moment.</p>
    </div>

    <div v-else>
      <table>
        <thead>
        <tr>
          <th>ID</th>
          <th>Title</th>
          <th>Description</th>
          <th>Genre</th>
          <th>Price</th>
          <th>Stock</th>
          <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="game in pendingGames" :key="game.gameId">
          <td>{{ game.gameId }}</td>
          <td>{{ game.title }}</td>
          <td>{{ game.description }}</td>
          <td>{{ game.genre }}</td>
          <td>{{ game.price }}</td>
          <td>{{ game.stock }}</td>
          <td>
            <button @click="approveGame(game.gameId)" class="approve-btn">Approve</button>
            <button @click="rejectGame(game.gameId)" class="reject-btn">Reject</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "ApproveGames",
  data() {
    return {
      pendingGames: [],
      loading: false,
    };
  },
  methods: {
    async fetchPendingGames() {
      this.loading = true;
      try {
        const response = await axios.get("http://localhost:8080/api/games/pending");
        this.pendingGames = response.data; // Populate the pendingGames array
      } catch (error) {
        console.error("Failed to fetch pending games:", error);
        alert("Failed to fetch pending games. Please try again later.");
      } finally {
        this.loading = false;
      }
    },
    async approveGame(gameId) {
      try {
        await axios.put(`http://localhost:8080/api/games/${gameId}/approve`);
        alert(`Game with ID ${gameId} has been approved.`);
        this.fetchPendingGames(); // Refresh the list of pending games
      } catch (error) {
        console.error("Failed to approve game:", error);
        alert(`Failed to approve game with ID ${gameId}.`);
      }
    },
    async rejectGame(gameId) {
      try {
        await axios.put(`http://localhost:8080/api/games/${gameId}/reject`);
        alert(`Game with ID ${gameId} has been rejected.`);
        this.fetchPendingGames();
      } catch (error) {
        console.error("Failed to reject game:", error);
        alert(`Failed to reject game with ID ${gameId}.`);
      }
    },
  },
  async created() {
    this.fetchPendingGames();
  },
};
</script>

<style scoped>
h1 {
    color:black;
}

.approve-games {
  font-family: Arial, sans-serif;
  padding: 20px;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin: 20px 0;
}

table th, table td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: center;
  color:black;
}

table th {
  background-color: #f4f4f4;
}

.approve-btn {
  background-color: #4CAF50;
  color: white;
  border: none;
  padding: 5px 10px;
  cursor: pointer;
  border-radius: 5px;
}

.reject-btn {
  background-color: #f44336;
  color: white;
  border: none;
  padding: 5px 10px;
  cursor: pointer;
  border-radius: 5px;
}

.approve-btn:hover {
  background-color: #45a049;
}

.reject-btn:hover {
  background-color: #e53935;
}
</style>
