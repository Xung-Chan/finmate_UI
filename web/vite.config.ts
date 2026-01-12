import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react-swc'
import path from 'path';

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [react()],
    server: {
    port: 5555,
    allowedHosts: [
      "unthinkable-unedacious-julianna.ngrok-free.dev",
      "https://nsqxjbdt-5555.asse.devtunnels.ms"
    ]
  },
    resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
   
})
