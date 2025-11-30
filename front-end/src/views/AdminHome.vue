<template>
  <div class="admin-home">
      <!-- 核心数据统计卡片 -->
      <div class="stats-grid">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: linear-gradient(135deg, #2C5F8D 0%, #1E4470 100%)">
              <i class="el-icon-user"></i>
            </div>
            <div class="stat-info">
              <div class="stat-label">用户总数</div>
              <div class="stat-value">{{ overviewData.userStats.total }}</div>
              <div class="stat-desc">正常: {{ overviewData.userStats.normal }} | 禁用: {{ overviewData.userStats.disabled }}</div>
            </div>
          </div>
        </el-card>

        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: linear-gradient(135deg, #5C8D89 0%, #3F6B67 100%)">
              <i class="el-icon-goods"></i>
            </div>
            <div class="stat-info">
              <div class="stat-label">商品总数</div>
              <div class="stat-value">{{ overviewData.productStats.total }}</div>
              <div class="stat-desc">上架: {{ overviewData.productStats.onSale }} | 下架: {{ overviewData.productStats.offSale }}</div>
            </div>
          </div>
        </el-card>

        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: linear-gradient(135deg, #8B6F47 0%, #6B532F 100%)">
              <i class="el-icon-shopping-cart-2"></i>
            </div>
            <div class="stat-info">
              <div class="stat-label">订单总数</div>
              <div class="stat-value">{{ overviewData.orderStats.total }}</div>
              <div class="stat-desc">总销售额: ¥{{ formatAmount(overviewData.orderStats.totalAmount) }}</div>
            </div>
          </div>
        </el-card>

        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: linear-gradient(135deg, #7D6B91 0%, #5E4F6E 100%)">
              <i class="el-icon-document-checked"></i>
            </div>
            <div class="stat-info">
              <div class="stat-label">保险订单</div>
              <div class="stat-value">{{ overviewData.insuranceStats.totalOrders }}</div>
              <div class="stat-desc">生效中: {{ overviewData.insuranceStats.activeOrders }} | 已过期: {{ overviewData.insuranceStats.expiredOrders }}</div>
            </div>
          </div>
        </el-card>

        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: linear-gradient(135deg, #6B8E23 0%, #556B2F 100%)">
              <i class="el-icon-date"></i>
            </div>
            <div class="stat-info">
              <div class="stat-label">医院预约</div>
              <div class="stat-value">{{ overviewData.hospitalStats.totalAppointments }}</div>
              <div class="stat-desc">待赴约: {{ overviewData.hospitalStats.pending }} | 已完成: {{ overviewData.hospitalStats.completed }}</div>
            </div>
          </div>
        </el-card>

        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: linear-gradient(135deg, #A0522D 0%, #8B4513 100%)">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stat-info">
              <div class="stat-label">理赔申请</div>
              <div class="stat-value">{{ overviewData.insuranceStats.totalClaims }}</div>
              <div class="stat-desc">待审核: {{ overviewData.insuranceStats.pendingClaims }} | 已打款: {{ overviewData.insuranceStats.paidClaims }}</div>
            </div>
          </div>
        </el-card>

        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: linear-gradient(135deg, #4A5D7C 0%, #364560 100%)">
              <i class="el-icon-collection"></i>
            </div>
            <div class="stat-info">
              <div class="stat-label">社区帖子</div>
              <div class="stat-value">{{ totalCommunityPosts }}</div>
              <div class="stat-desc">领养: {{ overviewData.communityStats.totalAdoptions }} | 配种: {{ overviewData.communityStats.totalBreedings }} | 寄养: {{ overviewData.communityStats.totalFosters }}</div>
            </div>
          </div>
        </el-card>

        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: linear-gradient(135deg, #8B7355 0%, #6B5636 100%)">
              <i class="el-icon-chat-line-square"></i>
            </div>
            <div class="stat-info">
              <div class="stat-label">论坛动态</div>
              <div class="stat-value">{{ overviewData.forumStats.totalPosts }}</div>
              <div class="stat-desc">点赞: {{ overviewData.forumStats.totalLikes }} | 评论: {{ overviewData.forumStats.totalComments }}</div>
            </div>
          </div>
        </el-card>
      </div>
      
      <!-- 图表区域 -->
      <div class="charts-section">
        <el-row :gutter="24">
          <el-col :span="8">
            <el-card class="chart-card">
              <div slot="header" class="chart-header">
                <span>订单状态分布</span>
              </div>
              <div id="orderStatusChart" class="chart-container"></div>
            </el-card>
          </el-col>
          
          <el-col :span="8">
            <el-card class="chart-card">
              <div slot="header" class="chart-header">
                <span>宠物类型分布</span>
              </div>
              <div id="petTypeChart" class="chart-container"></div>
            </el-card>
          </el-col>

          <el-col :span="8">
            <el-card class="chart-card">
              <div slot="header" class="chart-header">
                <span>理赔申请状态</span>
              </div>
              <div id="claimStatusChart" class="chart-container"></div>
            </el-card>
          </el-col>
        </el-row>
        
        <el-row :gutter="24" style="margin-top: 24px;">
          <el-col :span="12">
            <el-card class="chart-card">
              <div slot="header" class="chart-header">
                <span>商品分类分布</span>
              </div>
              <div id="categoryChart" class="chart-container"></div>
            </el-card>
          </el-col>

          <el-col :span="12">
            <el-card class="chart-card">
              <div slot="header" class="chart-header">
                <span>社区帖子分布</span>
              </div>
              <div id="communityChart" class="chart-container"></div>
            </el-card>
          </el-col>
        </el-row>
      </div>
  </div>
</template>

<script>
import { 
  getOverview, 
  getCategoryDistribution, 
  getOrderStatusDistribution, 
  getPetTypeDistribution, 
  getCommunityPostDistribution,
  getClaimStatusDistribution
} from '@/api/stats';

export default {
  name: 'AdminHome',
  data() {
    return {
      overviewData: {
        userStats: { total: 0, normal: 0, disabled: 0 },
        productStats: { total: 0, onSale: 0, offSale: 0 },
        orderStats: { total: 0, pending: 0, shipped: 0, completed: 0, totalAmount: 0 },
        insuranceStats: { 
          totalInsurances: 0, 
          totalOrders: 0, 
          activeOrders: 0, 
          expiredOrders: 0,
          totalClaims: 0,
          pendingClaims: 0,
          approvedClaims: 0,
          rejectedClaims: 0,
          paidClaims: 0
        },
        hospitalStats: { totalHospitals: 0, totalAppointments: 0, pending: 0, arrived: 0, completed: 0 },
        communityStats: { 
          totalAdoptions: 0, 
          activeAdoptions: 0, 
          totalBreedings: 0, 
          activeBreedings: 0,
          totalFosters: 0,
          activeFosters: 0
        },
        forumStats: { totalPosts: 0, totalLikes: 0, totalComments: 0 }
      }
    };
  },
  computed: {
    totalCommunityPosts() {
      return this.overviewData.communityStats.totalAdoptions + 
             this.overviewData.communityStats.totalBreedings + 
             this.overviewData.communityStats.totalFosters;
    }
  },
  mounted() {
    this.loadData();
  },
  methods: {
    /**
     * 加载所有统计数据
     */
    async loadData() {
      try {
        // 加载概览数据
        const overviewRes = await getOverview();
        this.overviewData = overviewRes.data;

        // 等待DOM更新后初始化图表
        this.$nextTick(() => {
          this.initCharts();
        });
      } catch (error) {
        console.error('加载数据失败:', error);
        this.$notify.error({
          title: '错误',
          message: '加载统计数据失败'
        });
      }
    },

    /**
     * 格式化金额
     */
    formatAmount(amount) {
      if (!amount) return '0.00';
      return Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    },

    /**
     * 初始化所有图表
     */
    async initCharts() {
      try {
        await Promise.all([
          this.initOrderStatusChart(),
          this.initPetTypeChart(),
          this.initClaimStatusChart(),
          this.initCategoryChart(),
          this.initCommunityChart()
        ]);
      } catch (error) {
        console.error('初始化图表失败:', error);
      }
    },
    
    /**
     * 初始化订单状态分布图表
     */
    async initOrderStatusChart() {
      try {
        const res = await getOrderStatusDistribution();
        const data = res.data;

        const chart = this.$echarts.init(document.getElementById('orderStatusChart'));
        const option = {
          tooltip: {
            trigger: 'item',
            formatter: '{b}: {c} ({d}%)'
          },
          legend: {
            orient: 'vertical',
            right: 10,
            top: 'center'
          },
          color: ['#2C5F8D', '#5C8D89', '#8B6F47'],
          series: [{
            name: '订单状态',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 18,
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: data
          }]
        };
        chart.setOption(option);

        // 响应式
        window.addEventListener('resize', () => {
          chart.resize();
        });
      } catch (error) {
        console.error('订单状态图表初始化失败:', error);
      }
    },
    
    /**
     * 初始化宠物类型分布图表
     */
    async initPetTypeChart() {
      try {
        const res = await getPetTypeDistribution();
        const data = res.data;

        const chart = this.$echarts.init(document.getElementById('petTypeChart'));
        const option = {
          tooltip: {
            trigger: 'item',
            formatter: '{b}: {c} ({d}%)'
          },
          legend: {
            orient: 'vertical',
            right: 10,
            top: 'center'
          },
          color: ['#7D6B91', '#6B8E23'],
          series: [{
            name: '宠物类型',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 18,
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: data
          }]
        };
        chart.setOption(option);

        // 响应式
        window.addEventListener('resize', () => {
          chart.resize();
        });
      } catch (error) {
        console.error('宠物类型图表初始化失败:', error);
      }
    },

    /**
     * 初始化理赔申请状态图表
     */
    async initClaimStatusChart() {
      try {
        const res = await getClaimStatusDistribution();
        const data = res.data;

        const chart = this.$echarts.init(document.getElementById('claimStatusChart'));
        const option = {
          tooltip: {
            trigger: 'item',
            formatter: '{b}: {c} ({d}%)'
          },
          legend: {
            orient: 'vertical',
            right: 10,
            top: 'center'
          },
          color: ['#A0522D', '#4A5D7C', '#8B7355', '#2C5F8D'],
          series: [{
            name: '理赔状态',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 18,
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: data
          }]
        };
        chart.setOption(option);

        // 响应式
        window.addEventListener('resize', () => {
          chart.resize();
        });
      } catch (error) {
        console.error('理赔状态图表初始化失败:', error);
      }
    },
    
    /**
     * 初始化商品分类分布图表
     */
    async initCategoryChart() {
      try {
        const res = await getCategoryDistribution();
        const data = res.data;

        const chart = this.$echarts.init(document.getElementById('categoryChart'));
        const option = {
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow'
            }
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            data: data.map(item => item.name),
            axisLine: {
              lineStyle: {
                color: '#999'
              }
            },
            axisLabel: {
              rotate: 30,
              interval: 0
            }
          },
          yAxis: {
            type: 'value',
            axisLine: {
              lineStyle: {
                color: '#999'
              }
            }
          },
          series: [{
            name: '商品数量',
            type: 'bar',
            data: data.map(item => item.value),
            itemStyle: {
              color: new this.$echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#5C8D89' },
                { offset: 1, color: '#3F6B67' }
              ])
            },
            barWidth: '50%'
          }]
        };
        chart.setOption(option);

        // 响应式
        window.addEventListener('resize', () => {
          chart.resize();
        });
      } catch (error) {
        console.error('商品分类图表初始化失败:', error);
      }
    },

    /**
     * 初始化社区帖子分布图表
     */
    async initCommunityChart() {
      try {
        const res = await getCommunityPostDistribution();
        const data = res.data;

        const chart = this.$echarts.init(document.getElementById('communityChart'));
        const option = {
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow'
            }
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            data: data.map(item => item.name),
            axisLine: {
              lineStyle: {
                color: '#999'
              }
            }
          },
          yAxis: {
            type: 'value',
            axisLine: {
              lineStyle: {
                color: '#999'
              }
            }
          },
          series: [{
            name: '帖子数量',
            type: 'bar',
            data: data.map(item => item.value),
            itemStyle: {
              color: new this.$echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#4A5D7C' },
                { offset: 1, color: '#364560' }
              ])
            },
            barWidth: '50%'
          }]
        };
        chart.setOption(option);

        // 响应式
        window.addEventListener('resize', () => {
          chart.resize();
        });
      } catch (error) {
        console.error('社区帖子图表初始化失败:', error);
      }
    }
  }
};
</script>

<style scoped>
.admin-home {
  padding: 20px;
}

/* 统计卡片 */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 24px;
}

.stat-card {
  border-radius: 8px;
  transition: all 0.3s ease;
  border: 1px solid #e8e8e8;
}

.stat-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.stat-content {
  display: flex;
  gap: 16px;
  align-items: center;
  padding: 20px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 28px;
  flex-shrink: 0;
}

.stat-info {
  flex: 1;
  min-width: 0;
}

.stat-label {
  color: #666;
  font-size: 14px;
  margin-bottom: 8px;
  font-weight: 500;
}

.stat-value {
  color: #333;
  font-size: 26px;
  font-weight: 600;
  margin-bottom: 6px;
  line-height: 1;
}

.stat-desc {
  font-size: 12px;
  color: #999;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 图表区域 */
.charts-section {
  margin-bottom: 24px;
}

.chart-card {
  border-radius: 8px;
  border: 1px solid #e8e8e8;
}

.chart-card >>> .el-card__header {
  padding: 16px 20px;
  border-bottom: 1px solid #e8e8e8;
}

.chart-header {
  font-weight: 600;
  font-size: 15px;
  color: #333;
}

.chart-container {
  width: 100%;
  height: 300px;
  padding: 10px;
}

/* 响应式设计 */
@media (max-width: 1600px) {
  .stats-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 1200px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .chart-container {
    height: 250px;
  }
}
</style>

