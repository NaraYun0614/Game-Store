<template>
  <div class="orders">
    <h1>Order Summary</h1>
    <div v-if="orders.length" class="order-container">
      <div v-for="order in orders" :key="order.orderId" class="order-box">
        <h2>Order #{{ order.orderId }}</h2>
        <p><strong>Price:</strong> ${{ order.price.toFixed(2) }}</p>
        <p><strong>Date:</strong> {{ formatDate(order.date) }}</p>
        <button class="view-details" @click="viewOrderDetails(order)">View Details</button>
      </div>
    </div>
    <p v-else class="no-orders">No orders available.</p>
  </div>
</template>

<script>
import axios from "axios";

const axiosClient = axios.create({
  baseURL: "http://localhost:8080", // Backend API base URL
});

export default {
  name: "Orders",
  data() {
    return {
      customer: {
        name: "",
        email: "",
        password: "",
      },
      orders: [], // Array to store fetched orders
    };
  },
  async created() {
    this.customer = JSON.parse(sessionStorage.getItem("customer"));
    console.log(this.customer.email);
    try {
      const response = await axiosClient.get(`/customers/getAllOrders/${this.customer.email}`);
      console.log("fetching data: ", response.data);
      this.orders = response.data;
    } catch (e) {
      console.error("Error getting the orders:", e.response?.data || e.message);
    }
  },
  methods: {
    // Format date to a readable format
    formatDate(dateString) {
      const options = { year: "numeric", month: "long", day: "numeric" };
      return new Date(dateString).toLocaleDateString(undefined, options);
    },
    // Handle "View Details" button click
    viewOrderDetails(order) {
      alert(`Viewing details for Order #${order.orderId}`);
      // Logic to navigate to or display order details can be added here
    },
  },
};
</script>

<style scoped>
/* Main container for the orders page */
.orders {
  max-width: 900px;
  margin: 0 auto;
  padding: 20px;
  text-align: center;
  font-family: Arial, sans-serif;
}

/* Page heading */
.orders h1 {
  font-size: 2rem;
  margin-bottom: 20px;
  color: #333;
}

/* Container for order boxes */
.order-container {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
}

/* Individual order box */
.order-box {
  background-color: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 10px;
  padding: 15px;
  width: 250px;
  text-align: left;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.order-box:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 10px rgba(0, 0, 0, 0.15);
}

/* Order box heading */
.order-box h2 {
  font-size: 1.5rem;
  margin-bottom: 10px;
  color: #555;
}

/* Order details */
.order-box p {
  font-size: 1rem;
  margin: 5px 0;
  color: #666;
}

/* View details button */
.view-details {
  display: inline-block;
  background-color: #007bff;
  color: white;
  padding: 8px 15px;
  margin-top: 10px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  text-align: center;
  font-size: 1rem;
}

.view-details:hover {
  background-color: #0056b3;
}

/* No orders message */
.no-orders {
  font-size: 1.2rem;
  color: #888;
  margin-top: 20px;
}
</style>
