<template>
  <div class = "game-manager">
    <header class = "header">
        <h1>Game List</h1>
        <!-- Button to navigate to create game -->
            <button class = "create-button" @click="createGame">Create New Game</button>
    </header>

    <div class = "game-list">
        <div class = "game-specific" v-for = "game in games" :key="game.gameId">
            <img
            :src="game.imageUrl || 'https://via.placeholder.com/150'"
            alt="Game Image"
            class="game-image"
            />
            <h2> {{ game.title }} </h2>
            <p class = "genre"> Genre: {{ game.genre }} </p>
            <p class = "price"> Price: {{ game.price }} </p>
            <p class = "stock"> Stock: {{ game.stock }} </p>

            <div class = "actions">
                <button class = "details-button" @click="gameDetail(game.gameId)">Detail</button>
                <button class = "edit-button" @click="editGame(game.gameId)">Edit</button>
                <button class = "del-button" @click="deleteGame(game.gameId)">Delete</button>
            </div>
        </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

const axiosClient = axios.create({
  baseURL: "http://localhost:8080", // Backend API base URL
});

export default {
  name: "GameList",
  data() {
    return {
      games: [], // Array to store games fetched from backend
    };
  },

  async created() {
    try {
      // Fetch games when the component is created
      const response = await axiosClient.get("/api/games");
      this.games = response.data.filter((game) => game.requestStatus === "Approved").map((game) => ({
        ...game,
        imageUrl: game.imageUrl || "",
      })); // Populate the games array with the response data
      // this.games = response.data.;
    } catch (e) {
      console.error("Error fetching games:", e); // Log any errors
    }
  },
  methods: {
    gameDetail(gameId) {
      this.$router.push({ name: "GameDetails", params: { id: gameId } });
    },
    createGame() {
      this.$router.push({ name: "CreateGame" });
    },
    editGame(gameId) {
      this.$router.push({ name: "UpdateGame", params: { id: gameId } });
    },
    async deleteGame(gameId) {
      if (!confirm("Are you sure you want to delete this game?")) {
        return;
      }
      try {
        await axiosClient.delete(`/api/games/${gameId}`); // Corrected template literal syntax
        this.games = this.games.filter((game) => game.gameId !== gameId);
        alert("Game deleted successfully!");
      } catch (error) {
        console.error("Error deleting game:", error);
        alert("Failed to delete game.");
      }
    },
    addGameLocally(game) {
      this.games.push({
        ...game,
        imageUrl: game.imageUrl || "https://via.placeholder.com/150", // Add default image URL
      });
    },
  },
};

</script>

<style>

.game-image {
  width: 100%;
  height: auto;
  border-radius: 8px;
  margin-bottom: 10px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #4CAF50;
  color: white;
  padding: 10px 20px;
}

.create-button {
  background-color: #fff;
  color: #4CAF50;
  border: none;
  padding: 10px 15px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 15px;
}

.create-button:hover {
  background-color: #ddd;
}

.game-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  grid-template-columns: repeat(4, 1fr);
  gap: 10px;
  padding: 20px;
  max-width: 1200px;
}

.game-specific {
  background-color: white;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.game-specific:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.game-specific h2 {
  font-size: 20px;
  margin-bottom: 10px;
  color:black;
}

.game-specific .genre,
.game-specific .price,
.game-specific .stock {
  font-size: 15px;
  margin: 5px 0;
}

.actions {
  margin-top: 15px;
}

button {
  padding: 8px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.details-button {
  background-color: #007BFF;
  color: white;
  margin-right: 5px;
}

.edit-button {
  background-color: #007BFF;
  color: white;
  margin-right: 5px;
}

.del-button {
  background-color: #007BFF;
  color: white;
}

button:hover {
  opacity: 0.8;
}

</style>
