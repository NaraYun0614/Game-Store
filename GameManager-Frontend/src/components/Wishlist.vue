<template>
  <div class="wishlist-page">
    <h1>My Wishlist</h1>
    <div class="game-items">
      <div v-for="game in wishlist" :key="game.gameId" class="game-box">
        <!-- Game Details -->
        <div class="game-details">
          <div class="details-left">
            <img
                :src="game.imageUrl || 'https://via.placeholder.com/150'"
                alt="Game Image"
                class="game-image"
            />
          </div>
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
  name: "wishlist",
  data() {
    return {
      customer: {
        name:"",
        email:"",
        password:"",
      },
      wishlist: [],
    };
  },
  async created() {
    try {
      this.customer = JSON.parse(sessionStorage.getItem("customer"));

      // to do
      await axiosClient.get(`/customers/${this.customer.email}/cartAll`)
          .then(response => {
            console.log(response.data)
            // Group games by ID and compute quantities
            const gameCounts = response.data.reduce((acc, gameCopy) => {
              if (!acc.find(g => g.gameId === gameCopy.gameDto.gameId)) {
                acc.push({ ...gameCopy.gameDto, quantity: 1 });
              } else {
                acc.find(g => g.gameId === gameCopy.gameDto.gameId).quantity++;
              }
              return acc;
            }, []);
            this.cart = gameCounts;
          });
    } catch (error) {
      console.error("Error fetching cart games:", error);
    }
  },

  methods: {

  },
};
</script>

<style scoped>
.wishlist-page {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto; /* Center the page */
}

h1 {
  text-align: center;
  margin-bottom: 20px;
}

.game-items {
  //display: flex; /* Use flexbox only for game items */
  //flex-wrap: wrap; /* Allow wrapping of items to the next row */
  gap: 20px; /* Add space between items */
  justify-content: flex-start; /* Align items to the left */
}

.game-box {
  display: flex; /* Horizontal layout for the game-box */
  flex-direction: column; /* Ensure items are aligned in a row */
  align-items: flex-start; /* Vertically align all items in the game-box */
  width: 100%; /* Full width for the container */
  border: 1px solid #ccc;
  border-radius: 8px;
  padding: 20px; /* Add padding for spacing */
  background-color: #f9f9f9;
  margin-bottom: 20px; /* Space between rows */
}

.game-image {
  width: 150px;
  height: 150px;
  object-fit: cover; /* Ensure the image scales properly */
  border-radius: 8px;
  margin-right: 20px; /* Space between the image and the details */
}

.game-details {
  flex: 1; /* Allow the details section to grow */
  display: flex; /* Use flexbox to structure the details */
  flex-direction: row; /* Keep title and controls side-by-side */
  align-items: center; /* Vertically align all content */
  justify-content: space-between; /* Space between title and controls */
}

.details-left {
  flex: 1; /* Allow the left section (title) to grow */
}

.details-left h2 {
  font-size: 18px;
  margin: 0; /* Remove extra margins for clean alignment */
}

.details-right {
  display: flex; /* Use flexbox for stacking */
  flex-direction: column; /* Stack price, quantity, and total vertically */
  align-items: flex-start; /* Center-align content in this column */
  justify-content: flex-start; /* Center-align vertically */
}

.quantity-controls {
  display: flex; /* Horizontal layout for buttons and quantity */
  align-items: center; /* Vertically align buttons with quantity */
  gap: 10px; /* Add spacing between buttons and quantity */
  margin-top: 10px; /* Add spacing above controls */
}

.quantity-controls button {
  padding: 5px 10px;
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.quantity-controls button:hover {
  background-color: #0056b3;
}

.quantity-controls span {
  font-size: 16px;
}
</style>

