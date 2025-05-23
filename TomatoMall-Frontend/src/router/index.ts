import { createRouter, createWebHashHistory } from "vue-router"

const router = createRouter({
    history: createWebHashHistory(),
    routes: [{
        path: '/',
        redirect: '/login',
    }, {
        path: '/login',
        component: () => import('../views/user/Login.vue'),
        meta: {title: '用户登录'}
    }, {
        path: '/register',
        component: () => import('../views/user/Register.vue'),
        meta: {title: '用户注册'}
    }, {
        path: '/home',
        redirect: '/dashboard',
        component: () => import('../views/Home.vue'),
        children: [
            {
                path: '/dashboard',
                name: 'Dashboard',
                component: () => import('../views/user/Dashboard.vue'),
                meta: {title: '个人信息'}
            },
            {
                path: '/allProduct',
                name: 'allProduct',
                component: () => import('../views/product/AllProduct.vue'),
                meta: { title: '商品列表' }
            },

            {
                path: '/createProduct',
                name: 'createProduct',
                component: () => import('../views/product/CreateProduct.vue'),
                meta: {
                    title: '创建商品',
                    permission: ['admin']
                }
            },
            {
                path: '/productDetail/:productId',
                name: 'productDetail',
                component: () => import('../views/product/ProductDetail.vue'),
                meta: { title: '商品详情' }
            },
            {
                path: '/changeProductInfo/:productId',
                name: 'changeProductInfo',
                component: () => import('../views/product/ChangeProductInfo.vue'),
                meta: {
                    title: '修改商品信息',
                    permission: ['admin']
                }
            },
            {
                path: '/stockManage/:productId',
                name:'stockManage',
                component: () => import('../views/product/StockManage.vue'),
                meta:{
                    title : '库存管理',
                    permission: ['admin']
                }
            }
        ]
    }, {
        path: '/404',
        name: '404',
        component: () => import('../views/NotFound.vue'),
        meta: {title: '404'}
    }, {
        path: '/:catchAll(.*)',
        redirect: '/404'
    }]
})

router.beforeEach((to, _, next) => {
    const token: string | null = sessionStorage.getItem('token');
    const role: string | null = sessionStorage.getItem('role')

    if (to.meta.title) {
        document.title = to.meta.title
    }

    if (token) {
        if (to.meta.permission) {
            if (to.meta.permission.includes(role!)) {
                next()
            } else {
                next('/404')
            }
        } else {
            next()
        }
    } else {
        if (to.path === '/login') {
            next();
        } else if (to.path === '/register') {
            next()
        } else {
            next('/login')
        }
    }
})


export { router }

