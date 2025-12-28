<template>
  <div class="home-container">
    <el-header class="header">
      <div class="header-content">
        <div class="logo">
          <h1>AIGC版权保护系统</h1>
        </div>
        <div class="nav-menu">
          <el-button type="primary" @click="$router.push('/login')">登录</el-button>
          <el-button @click="$router.push('/register')">注册</el-button>
        </div>
      </div>
    </el-header>

    <el-main class="main-content">
      <div class="hero-section">
        <div class="hero-content">
          <h2>基于联盟链的AIGC作品版权确权与转让管理系统</h2>
          <p class="hero-description">
            利用区块链技术为您的AI生成作品提供可信的版权保护，
            实现作品确权、转让和查询的全流程管理。
          </p>
          <div class="hero-actions">
            <el-button type="primary" size="large" @click="$router.push('/register')">
              立即注册
            </el-button>
            <el-button size="large" @click="scrollToFeatures">了解详情</el-button>
          </div>
        </div>
        <div class="hero-image">
          <img src="https://trae-api-sg.mchost.guru/api/ide/v1/text_to_image?prompt=Blockchain%20copyright%20protection%20system%20illustration%2C%20digital%20art%20style%2C%20shield%20icon%20with%20blockchain%20nodes%2C%20blue%20and%20purple%20color%20scheme%2C%20modern%20and%20professional%20design&image_size=landscape_16_9" alt="版权保护" />
        </div>
      </div>

      <div class="features-section" ref="featuresSection">
        <h3>系统特色</h3>
        <div class="features-grid">
          <div class="feature-card">
            <div class="feature-icon">🔗</div>
            <h4>区块链存证</h4>
            <p>基于联盟链技术，确保作品确权信息的真实性和不可篡改性</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">🎨</div>
            <h4>AIGC支持</h4>
            <p>支持文本、图像、音频、视频等多种AI生成作品的版权保护</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">⚡</div>
            <h4>快速确权</h4>
            <p>智能合约自动生成确权凭证，大大缩短传统版权登记周期</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">🔄</div>
            <h4>版权转让</h4>
            <p>支持版权转让功能，智能合约自动记录转让信息</p>
          </div>
        </div>
      </div>
    </el-main>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const featuresSection = ref<HTMLElement>()

const scrollToFeatures = () => {
  featuresSection.value?.scrollIntoView({ behavior: 'smooth' })
}
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.header {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  max-width: 1200px;
  margin: 0 auto;
  height: 100%;
}

.logo h1 {
  color: white;
  margin: 0;
  font-size: 24px;
}

.main-content {
  padding: 0;
}

.hero-section {
  display: flex;
  align-items: center;
  justify-content: space-between;
  max-width: 1200px;
  margin: 0 auto;
  padding: 80px 20px;
  color: white;
}

.hero-content {
  flex: 1;
  max-width: 600px;
}

.hero-content h2 {
  font-size: 48px;
  margin-bottom: 20px;
  line-height: 1.2;
}

.hero-description {
  font-size: 18px;
  line-height: 1.6;
  margin-bottom: 40px;
  opacity: 0.9;
}

.hero-actions {
  display: flex;
  gap: 20px;
}

.hero-image {
  flex: 1;
  text-align: center;
}

.hero-image img {
  max-width: 100%;
  height: auto;
  border-radius: 10px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.3);
}

.features-section {
  background: white;
  padding: 80px 20px;
  text-align: center;
}

.features-section h3 {
  font-size: 36px;
  margin-bottom: 60px;
  color: #333;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 40px;
  max-width: 1200px;
  margin: 0 auto;
}

.feature-card {
  background: #f8f9fa;
  padding: 40px 30px;
  border-radius: 10px;
  transition: transform 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-10px);
}

.feature-icon {
  font-size: 48px;
  margin-bottom: 20px;
}

.feature-card h4 {
  font-size: 20px;
  margin-bottom: 15px;
  color: #333;
}

.feature-card p {
  color: #666;
  line-height: 1.6;
}

@media (max-width: 768px) {
  .hero-section {
    flex-direction: column;
    text-align: center;
  }
  
  .hero-content h2 {
    font-size: 36px;
  }
  
  .hero-actions {
    justify-content: center;
  }
}
</style>