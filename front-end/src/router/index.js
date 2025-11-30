import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue')
  },
  {
    path: '/user',
    component: () => import('@/views/UserLayout.vue'),
    meta: { requiresAuth: true, role: '普通用户' },
    children: [
      {
        path: 'home',
        name: 'UserHome',
        component: () => import('@/views/UserHome.vue'),
        meta: { requiresAuth: true, role: '普通用户' }
      },
      {
        path: 'product-list',
        name: 'UserProductList',
        component: () => import('@/views/UserProductList.vue'),
        meta: { requiresAuth: true, role: '普通用户' }
      },
      {
        path: 'product-detail/:id',
        name: 'UserProductDetail',
        component: () => import('@/views/UserProductDetail.vue'),
        meta: { requiresAuth: true, role: '普通用户' }
      },
      {
        path: 'cart',
        name: 'UserCart',
        component: () => import('@/views/UserCart.vue'),
        meta: { requiresAuth: true, role: '普通用户' }
      },
      {
        path: 'order-list',
        name: 'UserOrderList',
        component: () => import('@/views/UserOrderList.vue'),
        meta: { requiresAuth: true, role: '普通用户' }
      },
      {
        path: 'review-list',
        name: 'UserReviewList',
        component: () => import('@/views/UserReviewList.vue'),
        meta: { requiresAuth: true, role: '普通用户' }
      },
      {
        path: 'insurance-list',
        name: 'UserInsuranceList',
        component: () => import('@/views/UserInsuranceList.vue'),
        meta: { requiresAuth: true, role: '普通用户' }
      },
      {
        path: 'insurance-detail/:id',
        name: 'UserInsuranceDetail',
        component: () => import('@/views/UserInsuranceDetail.vue'),
        meta: { requiresAuth: true, role: '普通用户' }
      },
      {
        path: 'policy-list',
        name: 'UserPolicyList',
        component: () => import('@/views/UserPolicyList.vue'),
        meta: { requiresAuth: true, role: '普通用户' }
      },
      {
        path: 'claim-list',
        name: 'UserClaimList',
        component: () => import('@/views/UserClaimList.vue'),
        meta: { requiresAuth: true, role: '普通用户' }
      },
      {
        path: 'hospital-list',
        name: 'UserHospitalList',
        component: () => import('@/views/UserHospitalList.vue'),
        meta: { requiresAuth: true, role: '普通用户' }
      },
      {
        path: 'hospital-detail/:id',
        name: 'UserHospitalDetail',
        component: () => import('@/views/UserHospitalDetail.vue'),
        meta: { requiresAuth: true, role: '普通用户' }
      },
      {
        path: 'appointment-list',
        name: 'UserAppointmentList',
        component: () => import('@/views/UserAppointmentList.vue'),
        meta: { requiresAuth: true, role: '普通用户' }
      },
      {
        path: 'breeding-list',
        name: 'UserBreedingList',
        component: () => import('@/views/UserBreedingList.vue'),
        meta: { requiresAuth: true, role: '普通用户' }
      },
      {
        path: 'breeding-detail/:id',
        name: 'UserBreedingDetail',
        component: () => import('@/views/UserBreedingDetail.vue'),
        meta: { requiresAuth: true, role: '普通用户' }
      },
      {
        path: 'foster-list',
        name: 'UserFosterList',
        component: () => import('@/views/UserFosterList.vue'),
        meta: { requiresAuth: true, role: '普通用户' }
      },
      {
        path: 'foster-detail/:id',
        name: 'UserFosterDetail',
        component: () => import('@/views/UserFosterDetail.vue'),
        meta: { requiresAuth: true, role: '普通用户' }
      },
      {
        path: 'adoption-list',
        name: 'UserAdoptionList',
        component: () => import('@/views/UserAdoptionList.vue'),
        meta: { requiresAuth: true, role: '普通用户' }
      },
      {
        path: 'adoption-detail/:id',
        name: 'UserAdoptionDetail',
        component: () => import('@/views/UserAdoptionDetail.vue'),
        meta: { requiresAuth: true, role: '普通用户' }
      },
      {
        path: 'forum',
        name: 'UserForumList',
        component: () => import('@/views/UserForumList.vue'),
        meta: { requiresAuth: true, role: '普通用户' }
      },
      {
        path: 'forum/detail',
        name: 'UserForumDetail',
        component: () => import('@/views/UserForumDetail.vue'),
        meta: { requiresAuth: true, role: '普通用户' }
      },
      {
        path: 'profile',
        name: 'UserProfile',
        component: () => import('@/views/UserProfile.vue'),
        meta: { requiresAuth: true, role: '普通用户' }
      },
      {
        path: 'change-password',
        name: 'ChangePassword',
        component: () => import('@/views/ChangePassword.vue'),
        meta: { requiresAuth: true, role: '普通用户' }
      }
    ]
  },
  {
    path: '/admin',
    component: () => import('@/views/AdminLayout.vue'),
    meta: { requiresAuth: true, role: '管理员' },
    children: [
      {
        path: 'home',
        name: 'AdminHome',
        component: () => import('@/views/AdminHome.vue'),
        meta: { requiresAuth: true, role: '管理员' }
      },
      {
        path: 'user-manage',
        name: 'AdminUserManage',
        component: () => import('@/views/AdminUserManage.vue'),
        meta: { requiresAuth: true, role: '管理员' }
      },
      {
        path: 'category-manage',
        name: 'AdminCategoryManage',
        component: () => import('@/views/AdminCategoryManage.vue'),
        meta: { requiresAuth: true, role: '管理员' }
      },
      {
        path: 'product-manage',
        name: 'AdminProductManage',
        component: () => import('@/views/AdminProductManage.vue'),
        meta: { requiresAuth: true, role: '管理员' }
      },
      {
        path: 'order-manage',
        name: 'AdminOrderManage',
        component: () => import('@/views/AdminOrderManage.vue'),
        meta: { requiresAuth: true, role: '管理员' }
      },
      {
        path: 'review-manage',
        name: 'AdminReviewManage',
        component: () => import('@/views/AdminReviewManage.vue'),
        meta: { requiresAuth: true, role: '管理员' }
      },
      {
        path: 'insurance-manage',
        name: 'AdminInsuranceManage',
        component: () => import('@/views/AdminInsuranceManage.vue'),
        meta: { requiresAuth: true, role: '管理员' }
      },
      {
        path: 'claim-manage',
        name: 'AdminClaimManage',
        component: () => import('@/views/AdminClaimManage.vue'),
        meta: { requiresAuth: true, role: '管理员' }
      },
      {
        path: 'hospital-manage',
        name: 'AdminHospitalManage',
        component: () => import('@/views/AdminHospitalManage.vue'),
        meta: { requiresAuth: true, role: '管理员' }
      },
      {
        path: 'appointment-manage',
        name: 'AdminAppointmentManage',
        component: () => import('@/views/AdminAppointmentManage.vue'),
        meta: { requiresAuth: true, role: '管理员' }
      },
      {
        path: 'breeding-manage',
        name: 'AdminBreedingManage',
        component: () => import('@/views/AdminBreedingManage.vue'),
        meta: { requiresAuth: true, role: '管理员' }
      },
      {
        path: 'foster-manage',
        name: 'AdminFosterManage',
        component: () => import('@/views/AdminFosterManage.vue'),
        meta: { requiresAuth: true, role: '管理员' }
      },
      {
        path: 'adoption-manage',
        name: 'AdminAdoptionManage',
        component: () => import('@/views/AdminAdoptionManage.vue'),
        meta: { requiresAuth: true, role: '管理员' }
      },
      {
        path: 'forum-manage',
        name: 'AdminForumManage',
        component: () => import('@/views/AdminForumManage.vue'),
        meta: { requiresAuth: true, role: '管理员' }
      },
      {
        path: 'forum-comment-manage',
        name: 'AdminForumCommentManage',
        component: () => import('@/views/AdminForumCommentManage.vue'),
        meta: { requiresAuth: true, role: '管理员' }
      },
      {
        path: 'system-setting',
        name: 'AdminSystemSetting',
        component: () => import('@/views/AdminSystemSetting.vue'),
        meta: { requiresAuth: true, role: '管理员' }
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 解决重复路由跳转报错问题
const originalPush = VueRouter.prototype.push
const originalReplace = VueRouter.prototype.replace

VueRouter.prototype.push = function push(location, onResolve, onReject) {
  if (onResolve || onReject) {
    return originalPush.call(this, location, onResolve, onReject)
  }
  return originalPush.call(this, location).catch(err => {
    // 忽略导航重复和导航取消错误
    if (err.name !== 'NavigationDuplicated' && err.name !== 'NavigationCancelled') {
      throw err
    }
  })
}

VueRouter.prototype.replace = function replace(location, onResolve, onReject) {
  if (onResolve || onReject) {
    return originalReplace.call(this, location, onResolve, onReject)
  }
  return originalReplace.call(this, location).catch(err => {
    // 忽略导航重复和导航取消错误
    if (err.name !== 'NavigationDuplicated' && err.name !== 'NavigationCancelled') {
      throw err
    }
  })
}

// 路由守卫
router.beforeEach((to, from, next) => {
  const userInfoStr = localStorage.getItem('userInfo')
  const userInfo = userInfoStr ? JSON.parse(userInfoStr) : null
  
  // 需要登录的页面
  if (to.meta.requiresAuth) {
    if (!userInfo) {
      // 未登录，跳转到登录页
      next('/login')
      return
    }
    
    if (to.meta.role && to.meta.role !== userInfo.role) {
      // 角色不匹配，跳转到对应角色的首页
      var targetPath = ''
      if (userInfo.role === '管理员') {
        targetPath = '/admin/home'
      } else {
        targetPath = '/user/home'
      }
      
      // 防止无限循环：如果目标路径就是要跳转的路径，则放行
      if (to.path === targetPath) {
        next()
        return
      }
      
      next(targetPath)
      return
    }
    
    // 验证通过
    next()
  } else {
    // 不需要登录的页面
    if (userInfo && (to.path === '/login' || to.path === '/register')) {
      // 已登录用户访问登录或注册页面，跳转到对应首页
      var homePath = ''
      if (userInfo.role === '管理员') {
        homePath = '/admin/home'
      } else {
        homePath = '/user/home'
      }
      next(homePath)
      return
    }
    
    next()
  }
})

export default router