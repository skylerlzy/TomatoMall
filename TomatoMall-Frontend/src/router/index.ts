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
            }, {
                path: '/allCartItem',
                name:'allCartItem',
                component: () => import('../views/cart/AllCartItem.vue'),
                meta:{
                    title : '查看购物车',
                }
            },{
                path: '/orderCheckOut',
                name:'orderCheckOut',
                component: () => import('../views/order/OrderCheckout.vue'),
                meta:{
                    title : '支付订单'
                }
            },{
                path:'/payForm/:orderId',
                name:'payForm',
                component: () => import('../views/order/PayForm.vue'),
                meta:{
                    title: '支付表单',
                }
            },{
                path:'/createAdvertisement',
                name:'createAdvertisement',
                component: () => import('../views/advertisement/CreateAdvertisement.vue'),
                meta:{
                    title: '创建广告',
                    permission: ['admin']
                }
            },  {
                path: '/advertisementDetail/:id',
                name:'advertisementDetail',
                component: () => import('../views/advertisement/AdvertisementDetail.vue'),
                meta:{
                    title : '广告商品详情',
                }
            },
            {
                path:'/allAdvertisement',
                name:'allAdvertisement',
                component: () => import('../views/advertisement/AllAdvertisement.vue'),
                meta:{
                    title: '广告详情',
                    permission: ['admin']
                }
            }, {
                path: '/changeAdvertisement/:id',
                name: 'changeAdvertisement',
                component: () => import('../views/advertisement/ChangeAdvertisement.vue'),
                meta: {
                    title: '修改商品信息',
                    permission: ['admin']
                }
            },
            {
                path:'/vipCenter',
                name:'vipCenter',
                component: () => import('../views/user/VipCenter.vue'),
                meta:{
                    title: '会员中心',
                }
            },
            {
                path:'/vipOrderCheckout',
                name:'vipOrderCheckout',
                component: () => import('../views/order/VIPOrderCheckout.vue'),
                meta:{
                    title: '会员订单',
                }
            },{
                path: '/allCoupon',
                name: 'allCoupon',
                component: () => import('../views/coupon/AllCoupon.vue'),
                meta: {
                    title: '优惠券列表',
                }
            },
            {
                path: '/createCoupon',
                name: 'createCoupon',
                component: () => import('../views/coupon/CreateCoupon.vue'),
                meta: {
                    title: '创建优惠券',
                }
            },
            {
                path: '/collection',
                name: 'Collection',
                component: () => import('../views/user/Collection.vue'),
                meta: {
                    requiresAuth: true
                }
            },
            {
                path: '/walletCenter',
                name: 'WalletCenter',
                component: () => import('../views/user/WalletCenter.vue'),
                meta: {
                    title: '钱包中心',
                }
            },
            {
                path:'/walletPayForm/:orderId',
                name:'WalletPayForm',
                component: () => import('../views/order/WalletPayForm.vue'),
                meta:{
                    title: '支付表单',
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

