<template>
    <div>
      <!-- Background Animations -->
      <div class="background-animations">
        <div class="circle circle-1"></div>
        <div class="circle circle-2"></div>
        <div class="circle circle-3"></div>
        <div class="square square-1"></div>
        <div class="square square-2"></div>
      </div>
  
      <!-- Search Game Section -->
      <div class="search-game">
        <h2>Search Games</h2>
    
        <!-- Search Form -->
        <div>
          <input
            type="text"
            v-model="searchKeyword"
            placeholder="Enter keyword to search"
          />
          <button @click="searchGamesByKeyword">Search</button>
        </div>
        <!-- Second Search Box -->
        <div>
          <input
              type="text"
              v-model="searchCategory"
              placeholder="Enter Category to search"
          />
          <button @click="searchGamesByCategory">Search</button>
        </div>

    <!-- Sort Options -->
        <div v-if="games.length > 0" class="sort-options">
        <select v-model="sortOption">
            <option value="price">Sort by Price</option>
            <option value="title">Sort Alphabetically</option>
        </select>
        <button @click="sortGames">Sort</button>
        </div>

        <!-- Results Section -->
        <div v-if="games.length > 0">
          <h3>Search Results:</h3>
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Title</th>
                <th>Description</th>
                <th>Genre</th>
                <th>Price</th>
                <th>Stock</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="game in games" :key="game.id">
                <td>{{ game.id }}</td>
                <td>{{ game.title }}</td>
                <td>{{ game.description }}</td>
                <td>{{ game.genre }}</td>
                <td>{{ game.price }}</td>
                <td>{{ game.stock }}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- No Results Message -->
        <div v-else-if="searchPerformed">
          <p>No games found for "{{ searchKeyword }}".</p>
        </div>
      </div>
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
        searchKeyword: "", // Keyword for searching games
        searchCategory: "",
        games: [], // Array to hold search results
        searchPerformed: false, // Indicates if a search has been performed
        sortOption: "price",
      };
    },
    methods: {
      async searchGamesByKeyword() {
        if (!this.searchKeyword.trim()) {
          alert("Please enter a keyword to search.");
          return;
        }

        try {
          const response = await axiosClient.get("/api/games/search", {
            params: { keyword: this.searchKeyword || null,
              category: this.searchCategory || null,
              sortBy: this.sortOption },
          });
          this.games = response.data.filter((game) => game.requestStatus === "Approved");
          this.searchPerformed = true;
        } catch (error) {
          console.error("Error searching games:", error);
          alert("An error occurred while searching for games.");
        }
      },
      async searchGamesByCategory() {
        if (!this.searchCategory.trim()) {
          alert("Please enter a category to search.");
          return;
        }
        try {
          const response = await axiosClient.get("/api/games/search", {
            params: { keyword: null,
              category: this.searchCategory,
              sortBy: this.sortOption },
          });
          this.games = response.data.filter((game) => game.requestStatus === "Approved");
          this.searchPerformed = true;
        } catch (error) {
          console.error("Error searching games:", error);
          alert("An error occurred while searching for games.");
        }
      },
      sortGames() {
      if (this.sortOption === "price") {
        this.games.sort((a, b) => a.price - b.price);
      } else if (this.sortOption === "title") {
        this.games.sort((a, b) =>
          a.title.localeCompare(b.title, undefined, { sensitivity: "base" })
        );
      }
    },
  }
}
  </script>
  
  <style scoped>
    h2, h3, table th, table td {
    color: black; /* Set font color for headings */
    }

  /* Background Animations */
  .background-animations {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: -1; /* Ensure it stays behind the main content */
    overflow: hidden;
  }
  
  .circle {
    position: absolute;
    border-radius: 50%;
    animation: float 5s infinite ease-in-out;
    opacity: 0.5;
  }
  
  .circle-1 {
    width: 100px;
    height: 100px;
    background-color: rgba(255, 0, 0, 0.2);
    top: 10%;
    left: 20%;
    animation-delay: 0s;
  }
  
  .circle-2 {
    width: 150px;
    height: 150px;
    background-color: rgba(0, 255, 0, 0.2);
    top: 40%;
    left: 50%;
    animation-delay: 1s;
  }
  
  .circle-3 {
    width: 200px;
    height: 200px;
    background-color: rgba(0, 0, 255, 0.2);
    top: 70%;
    left: 30%;
    animation-delay: 2s;
  }
  
  .square {
    position: absolute;
    background-color: rgba(0, 0, 0, 0.1);
    animation: float 6s infinite ease-in-out;
    opacity: 0.5;
  }
  
  .square-1 {
    width: 120px;
    height: 120px;
    top: 15%;
    left: 75%;
    animation-delay: 1s;
  }
  
  .square-2 {
    width: 80px;
    height: 80px;
    top: 60%;
    left: 10%;
    animation-delay: 3s;
  }
  
  /* Floating Animation */
  @keyframes float {
    0% {
      transform: translateY(0) scale(1);
    }
    50% {
      transform: translateY(-20px) scale(1.1);
    }
    100% {
      transform: translateY(0) scale(1);
    }
  }
  
  /* Main Content Styling */
  .search-game {
    position: relative;
    z-index: 1; /* Ensure it's above the background animations */
  }
  
  table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 20px;
  }
  
  th,
  td {
    border: 1px solid #ddd;
    padding: 8px;
    text-align: left;
  }
  
  th {
    background-color: #f4f4f4;
  }
  </style>
  