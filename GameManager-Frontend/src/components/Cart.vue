<template>
  <div class="cart-page">
    <h1>My Cart</h1>
    <!-- Game Items Container -->
    <div class="game-items">
      <div v-for="game in cart" :key="game.gameId" class="game-box">
        <!-- Game Details -->
        <div class="game-details">
          <!-- Left Section: Title -->
          <div class="details-left">
            <img
                :src="game.imageUrl || 'https://via.placeholder.com/150'"
                alt="Game Image"
                class="game-image"
            />
          </div>
          <!-- Right Section: Price, Quantity, and Controls -->
          <div class="details-right">
            <h1>{{ game.title }}</h1>
            <div class="quantity-controls">
              <button @click="decreaseQuantity(game)">-</button>
              <span>{{ game.quantity }}</span>
              <button @click="increaseQuantity(game)">+</button>
            </div>
            <p>Total: ${{ (game.quantity * game.price).toFixed(2) }}</p>
          </div>
        </div>
      </div>
    </div>
    <!-- Checkout Box -->
    <div class="checkout-box">
      <button @click="checkout">Proceed to Checkout</button>
      <p>Total Price: ${{ totalCartPrice }}</p>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import Payment from "@/components/Payment.vue";
const axiosClient = axios.create({
  baseURL: "http://localhost:8080", // Backend API base URL
});
export default {
  name: "cart",
  data() {
    return {
      customer: {
        name:"",
        email:"",
        password:"",
      },
      cart: [],
    };
  },
  async created() {
    try {
      this.customer = JSON.parse(sessionStorage.getItem("customer"));
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
  computed: {
    totalCartPrice() {
      return this.cart.reduce((total, game) => total + game.price * game.quantity, 0).toFixed(2);
    },
  },
  methods: {
    async increaseQuantity(game) {
      try {
        const gameCopyResponse = await axiosClient.post(`/game-copy`, game);
        console.log('GameCopy created:', gameCopyResponse.data);
        await axiosClient.put(`/customers/addCart/${gameCopyResponse.data.gameCopyId}`, this.customer);
        game.quantity++; // Update the local quantity
      } catch (error) {
        console.error('Error increasing item quantity:', error.response?.data || error.message);
        throw error;
      }
    },
    async decreaseQuantity(game) {
      try {
        if (game.quantity > 1) {
          const response = await axiosClient.put(`/customers/removeInCart/${game.gameId}`, this.customer, {
            headers: { 'Content-Type': 'application/json' },
          });
          game.quantity--; // Update the local quantity
          return response.data;
        } else {
          // If quantity reaches zero, remove the game from cart
          this.cart = this.cart.filter((item) => item.gameId !== game.gameId);
          await axiosClient.put(`/customers/removeInCart/${game.gameId}`, this.customer, {
            headers: { 'Content-Type': 'application/json' },
          });
        }
      } catch (error) {
        console.error('Error decreasing item quantity:', error.response?.data || error.message);
        throw error;
      }
    },
    async checkout() {
      // Get payment details from localStorage
      const paymentDetails = JSON.parse(localStorage.getItem("paymentDetails"));

      // Check if payment details exist
      if (paymentDetails && paymentDetails.customer_email === this.customer.email) {
        if (this.totalCartPrice > 0) {
          alert("Proceeding to checkout with total: $" + this.totalCartPrice);
          const response = await axiosClient.get(`/customers/${this.customer.email}/cartAll`)
          const purchaseOrder = {
            orderStatus: "Bought",
            price: this.totalCartPrice,
            games: response.data
          }
          console.log("purchase Order content: ", purchaseOrder)
          const postOrderResponse = await axiosClient.post("/api/orders", purchaseOrder)
          console.log("order created: ",postOrderResponse.data)
          await axiosClient.put(`/customers/addOrder/${postOrderResponse.data.orderId}`, this.customer);

          await axiosClient.put("/customers/clearCart", this.customer);
          this.$router.push("/orders")
        } else {
          alert("The price is zero. Please add items to your cart.");
        }
        // localStorage.clear();
      } else {
        this.$router.push("/payment");
      }
    },

  },
};
</script>

<style scoped>
.cart-page {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto; /* Center the cart page */
}

h1 {
  text-align: center;
  margin-bottom: 20px;
  color:black;
}

.game-items {
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

.checkout-box {
  border-top: 2px solid #ccc;
  padding-top: 20px;
  margin-top: 20px;
  text-align: center; /* Center the checkout box */
}

.checkout-box button {
  padding: 10px 20px;
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
}

.checkout-box button:hover {
  background-color: #0056b3;
}
</style>

