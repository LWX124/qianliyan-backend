# REST API Endpoint Inventory — All Services

---

## Service 1: web0508 (C-End App Server)

**Base context path:** `/cServer`  
**Port:** `8081`  
**Package:** `com.cheji.web`

### AccidentRecordController — base `/AccidentRecord`
| Method | Path | Description |
|--------|------|-------------|
| GET/POST | `/AccidentRecord/test` | Test endpoint |
| GET | `/AccidentRecord/list` | List accident records |
| GET | `/AccidentRecord/listDetails` | Accident record detail list |
| GET | `/AccidentRecord/updateState` | Update state |
| GET | `/AccidentRecord/todayRedEnvelope` | Today's red envelope info |
| GET | `/AccidentRecord/fans` | Fans list |
| GET | `/AccidentRecord/redEnvelope` | Red envelope records |
| GET | `/AccidentRecord/works` | Works list |
| GET | `/AccidentRecord/like` | Like action |
| POST | `/AccidentRecord/addVideo` | Add video |
| GET | `/AccidentRecord/videoDetails` | Video details |

### AndroidDownController — base `/android`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/android/checkVersion` | Check Android APK version |

### AppAuctionAddressController — base `/address`
| Method | Path | Description |
|--------|------|-------------|
| POST | `/address/addAddress` | Add delivery address |
| GET | `/address/queryAddress` | Query address list |
| GET | `/address/delAddress` | Delete address |
| GET | `/address/getCity` | Get city list |

### AppAuctionAliPayController — base `/auction/alipay`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/auction/alipay/createVipOrder` | Create Alipay VIP order |

### AppAuctionBidController — base `/auctionBid`
| Method | Path | Description |
|--------|------|-------------|
| POST | `/auctionBid/bidLog` | Submit auction bid |
| GET | `/auctionBid/bidRecord` | Query bid records |
| POST | `/auctionBid/fixedPrice` | Place fixed-price (buy now) bid |

### AppAuctionBrandSubController — base `/brandSub`
| Method | Path | Description |
|--------|------|-------------|
| POST | `/brandSub/addBrand` | Subscribe to car brand |
| GET | `/brandSub/delBrand` | Remove brand subscription |
| GET | `/brandSub/queryBrand` | Query brand subscriptions |

### AppAuctionCashOutController — base `/auctionCashOut`
| Method | Path | Description |
|--------|------|-------------|
| POST | `/auctionCashOut/addCashOut` | Apply for cash-out |

### AppAuctionContrlloer — base `/appAuction`
| Method | Path | Description |
|--------|------|-------------|
| POST | `/appAuction/getMinAppToken` | Get mini-program token |
| POST | `/appAuction/addAuctionCar` | Add auction car listing |
| POST | `/appAuction/setTransfer` | Set transfer info |
| GET | `/appAuction/getTransfer` | Get transfer info |
| GET | `/appAuction/auctionHot` | Hot auction listings |
| GET | `/appAuction/auctionRandom` | Random auction listings |
| GET | `/appAuction/myAuction` | My auction listings |
| GET | `/appAuction/detail` | Auction car detail |
| GET | `/appAuction/detail2` | Auction car detail (v2) |
| POST | `/appAuction/todayNewCar` | Today's new cars |
| GET | `/appAuction/collectHandle` | Toggle collect/bookmark |
| POST | `/appAuction/getAuctionList` | Get auction list (filtered) |
| GET | `/appAuction/getMyAuctionList` | Get my auction list |
| GET | `/appAuction/countMyAuction` | Count my auction items |
| GET | `/appAuction/collectList` | My collected listings |
| GET | `/appAuction/bidList` | My bid list |
| GET | `/appAuction/waitTransfer` | Pending transfer items |
| GET | `/appAuction/transfered` | Completed transfers |
| GET | `/appAuction/getCity` | Get city options |
| GET | `/appAuction/search` | Search auctions |
| GET | `/appAuction/getCarCount` | Get car count stats |

### AppAuctionCounselorController — base `/counselor`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/counselor/delCounselor` | Remove counselor |
| GET | `/counselor/queryCounselor` | Query counselors |

### AppAuctionFeedBackController — base `/feedback`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/feedback/subFeedback` | Submit user feedback |

### AppAuctionFindCarController — base `/findcar`
| Method | Path | Description |
|--------|------|-------------|
| POST | `/findcar/addFindCar` | Post a "find car" request |
| GET | `/findcar/queryFindCar` | Query find-car requests |
| GET | `/findcar/delFindCar` | Delete find-car request |

### AppAuctionIdentifyController — base `/identify`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/identify/checkDriving` | Verify driver's license |
| GET | `/identify/checkIDCard` | Verify ID card |
| GET | `/identify/checkCharter` | Verify business charter |
| GET | `/identify/getVin` | Get VIN information |

### AppAuctionMessageIdentifyController — base `/authentication`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/authentication/getAuthentication` | Get authentication info |
| POST | `/authentication/addAuthentication` | Submit authentication |
| GET | `/authentication/sendMessage` | Send verification SMS |

### AppAuctionMyMoneyController — base `/mymoney`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/mymoney/bail` | Bail/deposit info |
| GET | `/mymoney/servicefee` | Service fee records |
| GET | `/mymoney/signRecord` | Sign-in records |

### AppAuctionOrderController — base `/auctionOrder`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/auctionOrder/queryOrder` | Query auction orders |

### AppAuctionVipLvController — base `/auctionVip`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/auctionVip/vipExplain` | VIP level description |
| GET | `/auctionVip/vipInfoList` | VIP level list |

### AppAuctionWarnCarController — base `/warnCar`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/warnCar/addWarnCar` | Add car watch/alert |
| GET | `/warnCar/delWarnCar` | Remove car watch |
| GET | `/warnCar/queryWarnCar` | Query watched cars |

### AppAuctionWithdrawCashController — base `/auctionWithdrawCash`
| Method | Path | Description |
|--------|------|-------------|
| POST | `/auctionWithdrawCash/applyFor` | Apply for withdrawal |
| POST | `/auctionWithdrawCash/cancelApplyFor` | Cancel withdrawal application |

### AppAuctionWxPayController — base `/auction/wxPay`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/auction/wxPay/createVipOrder` | Create WeChat Pay VIP order |
| POST | `/auction/wxPay/createOnePriceOrder` | Create fixed-price order |
| POST | `/auction/wxPay/queryOnePriceOrderStatus` | Query fixed-price order status |
| GET | `/auction/wxPay/createBailOrder` | Create deposit/bail order |
| POST | `/auction/wxPay/payLogNotify` | WeChat Pay callback (pay log) |
| POST | `/auction/wxPay/bailLogNotify` | WeChat Pay callback (bail) |
| POST | `/auction/wxPay/onePriceNotify` | WeChat Pay callback (fixed price) |
| POST | `/auction/wxPay/queryOrderStatus` | Query order payment status |
| POST | `/auction/wxPay/queryBailOrderStatus` | Query bail order status |
| GET | `/auction/wxPay/getPayAmount` | Get payable amount |
| GET | `/auction/wxPay/getInfo` | Get payment info |

### AppBeautyPriceDetailController — base `/beautyPriceDetail`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/beautyPriceDetail/findBeautyMerchants` | Find beauty-service merchants |
| GET | `/beautyPriceDetail/beautyMerchantsDetails` | Beauty merchant details |

### AppBUserConfigController — base `/buserConfig`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/buserConfig/saveBId` | Save merchant binding config |

### AppClaimTeacherController — base `/appClaimTeacher`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/appClaimTeacher/findAccidMessBill` | Find accident message bills |
| GET | `/appClaimTeacher/ClaimTeaMessage` | Claim teacher messages |

### AppRescueIndentController — base `/appRescueIndent`
| Method | Path | Description |
|--------|------|-------------|
| POST | `/appRescueIndent/saveTakeElectricity` | Save roadside electricity rescue order |
| POST | `/appRescueIndent/calculateRescuePrice` | Calculate rescue pricing |
| GET | `/appRescueIndent/rescueDetails` | Rescue order details |
| GET | `/appRescueIndent/waitRescueTechnician` | Waiting rescue technician assignment |

### AppSendOutSheetController — base `/appSendOutSheet`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/appSendOutSheet/searchCMessageList` | Search dispatch message list |
| GET | `/appSendOutSheet/cMessageList` | Dispatch message list |
| GET | `/appSendOutSheet/cMessageDetails` | Dispatch message details |
| GET | `/appSendOutSheet/acceptTask` | Accept dispatch task |
| POST | `/appSendOutSheet/checkSign` | Upload sign-off check |
| GET | `/appSendOutSheet/find4sStrore` | Find 4S stores |
| POST | `/appSendOutSheet/saveCarMessage` | Save car message info |
| GET | `/appSendOutSheet/findMessageCar` | Find message-bound car |
| GET | `/appSendOutSheet/allBrandAndRecommd` | All brands and recommendations |
| GET | `/appSendOutSheet/pushMerchants` | Push order to merchants |
| GET | `/appSendOutSheet/isPutMessage` | Check if message is published |
| GET | `/appSendOutSheet/updateLocatioin` | Update location |
| POST | `/appSendOutSheet/accidentSer` | Accident service action |
| GET | `/appSendOutSheet/findPushMerchants` | Find pushed merchants |

### AppSendSheetController — base `/appSendSheet`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/appSendSheet/list` | Dispatch sheet list |
| GET | `/appSendSheet/details` | Dispatch sheet details |
| GET | `/appSendSheet/evaluation` | Evaluation info |

### AppSprayPaintIndentController — base `/appSprayPaintIndent`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/appSprayPaintIndent/sprayPaint` | Spray paint service info |
| GET | `/appSprayPaintIndent/sprayPaintMerchants` | Spray paint merchants |
| GET | `/appSprayPaintIndent/maintenanceProcess` | Maintenance process status |
| GET | `/appSprayPaintIndent/sprayPaintDetails` | Spray paint order details |
| GET | `/appSprayPaintIndent/cancelOrder` | Cancel spray paint order |
| GET | `/appSprayPaintIndent/waitTechnician` | Waiting technician assignment |

### AppSubstituteDrivingIndentController — base `/appSubstituteDrivingIndent`
| Method | Path | Description |
|--------|------|-------------|
| POST | `/appSubstituteDrivingIndent/saveSubstituteDrivingIndentIndent` | Save designated driving order |
| POST | `/appSubstituteDrivingIndent/getSubDrivePrice` | Get designated driving price |
| GET | `/appSubstituteDrivingIndent/subDirvingDetails` | Designated driving order details |
| GET | `/appSubstituteDrivingIndent/findSubTechnician` | Find available designated driver |
| GET | `/appSubstituteDrivingIndent/confirmSubDriIndent` | Confirm designated driving order |

### AppSubUsualAddressController — base `/appSubUsualAddress`
| Method | Path | Description |
|--------|------|-------------|
| POST | `/appSubUsualAddress/saveAddress` | Save common address |
| GET | `/appSubUsualAddress/inquireAddress` | Query common addresses |

### AppUserBMessageController — base `/appUserBMessage`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/appUserBMessage/findTechnician` | Find service technicians |
| GET | `/appUserBMessage/technicianDetails` | Technician details |

### AppUserCouponController — base `/appUserCoupon`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/appUserCoupon/couponList` | User coupon list |
| GET | `/appUserCoupon/couponDetails` | Coupon details |

### AppYearCheckIndentController — base `/appYearCheckIndent`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/appYearCheckIndent/yearCheckDetails` | Annual inspection order details |
| GET | `/appYearCheckIndent/yearCheckPrice` | Annual inspection pricing |
| GET | `/appYearCheckIndent/waitYearIndentTechnician` | Waiting technician for annual check |

### BizAccidentController — base `/bizAccident`
_(No endpoint methods defined — class body is empty)_

### CityController — base `/city`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/city/recommendCity` | Get recommended cities |
| GET | `/city/recommendCity22` | Get recommended cities (v2) |

### CleanIndetController — base `/cleanIndet`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/cleanIndet/cleanIndentDetails` | Car wash order details |

### CleanPriceDetailController — base `/cleanPriceDetail`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/cleanPriceDetail/findCleanMerchants` | Find car wash merchants |
| GET | `/cleanPriceDetail/merchantsCleanDetails` | Merchant car wash details |
| GET | `/cleanPriceDetail/merchantsPricesDetails` | Merchant pricing details |

### IndentController — base `/indent`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/indent/indentMes` | Order message info |
| POST | `/indent/save` | Save/create order |
| GET | `/indent/indentList22` | Order list (v2) |
| GET | `/indent/indentList` | Order list |
| GET | `/indent/IndentDetails` | Order details |
| GET | `/indent/confirmOrder` | Confirm order |
| GET | `/indent/otherIndentList` | Other order list |
| GET | `/indent/selectCarType` | Select car type |

### IndexController — base `/index`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/index/getXXData` | Get homepage data |

### LeagueController — base `/league`
| Method | Path | Description |
|--------|------|-------------|
| POST | `/league/save` | Save franchise/league application |
| GET | `/league/backWeb` | Back to web page |
| GET | `/league/backVideoWeb` | Back to video web page |
| GET | `/league/getShareTitle` | Get share title |
| GET | `/league/shareCount` | Share count |

### LoginController — base `/user`
| Method | Path | Description |
|--------|------|-------------|
| POST | `/user/getRegisterCode` | Get registration SMS code |
| POST | `/user/logOutCode` | Get logout verification code |
| POST | `/user/getLoginCode` | Get login SMS code |
| POST | `/user/getBindingSmsCode` | Get phone binding SMS code |
| POST | `/user/convertPhone` | Convert phone number |
| POST | `/user/register` | User registration |
| POST | `/user/loginForPass` | Login with password |
| POST | `/user/loginForCode` | Login with SMS code |
| POST | `/user/wxAccessToken` | WeChat access token exchange |
| POST | `/user/bindPhone` | Bind phone number |
| POST | `/user/smsForBindPhone` | SMS for phone binding |
| POST | `/user/smsForgetPass` | SMS for forgot password |
| POST | `/user/forgetUpdatePass` | Reset forgotten password |
| POST | `/user/signOut` | Sign out |

### MerchantsCommentsTreeController — base `/merchantsCommentsTree`
| Method | Path | Description |
|--------|------|-------------|
| POST | `/merchantsCommentsTree/saveComment` | Save merchant comment |
| POST | `/merchantsCommentsTree/saveCleanComment` | Save car wash comment |
| GET | `/merchantsCommentsTree/showComment` | Show comments |
| GET | `/merchantsCommentsTree/reportComment` | Report comment |

### MerchantsTreeController — base `/merchantsTree`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/merchantsTree/listall` | List all merchants by city |
| GET | `/merchantsTree/garage` | Repair shop merchants |
| GET | `/merchantsTree/allBrand` | All car brands |
| GET | `/merchantsTree/foursStoresBrand` | 4S stores and specialty shops |
| GET | `/merchantsTree/merchantsDetails` | Merchant details |
| GET | `/merchantsTree/merchantsComment` | Merchant comments |
| GET | `/merchantsTree/recommend` | Recommended merchants |
| GET | `/merchantsTree/recommendMerchants` | Recommended merchants list |
| GET | `/merchantsTree/lableDetailsList` | Label/tag details list |
| GET | `/merchantsTree/sotreDisplay` | Store display info |
| GET | `/merchantsTree/bindEmployee` | Bind employee to merchant |

### PageController — base `/page` (returns HTML views)
| Method | Path | Description |
|--------|------|-------------|
| GET | `/page/auctionAgreement` | Auction agreement page |
| GET | `/page/agreement` | Agreement page |
| GET | `/page/register1` | Registration page |
| GET | `/page/privacy` | Privacy policy page |
| GET | `/page/pair` | Pair page |
| GET | `/page/userAgreement` | User agreement page |
| GET | `/page/rescueAgreement` | Rescue service agreement |
| GET | `/page/yearCheckAgreement` | Annual check agreement |
| GET | `/page/subDrivingAgreement` | Designated driving agreement |
| GET | `/page/upgradeRules` | Upgrade rules page |
| GET | `/page/share` | H5 video share page |
| GET | `/page/accWebs` | Accident web page |

### PushBillController — base `/pushBill`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/pushBill/billList` | Push bill list |

### User2Controller — base `/user`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/user/getBankList` | Get bank card list |
| POST | `/user/addBank` | Add bank card |
| POST | `/user/addCashOut` | Apply for cash-out |

### UserController — base `/user`
| Method | Path | Description |
|--------|------|-------------|
| POST | `/user/update` | Update user profile |
| GET | `/user/myWallet` | My wallet info |
| GET | `/user/myTeam` | My team info |
| GET | `/user/personalCenter` | Personal center |
| GET | `/user/userBankList` | User bank card list |
| GET | `/user/ChangeList` | Change records list |
| GET | `/user/withdrawalDetails` | Withdrawal details |
| GET | `/user/plusUser` | Plus membership info |
| GET | `/user/sellList` | Sell list |
| GET | `/user/openPlusUser` | Open plus membership |
| GET | `/user/removeCard` | Remove bank card |
| GET | `/user/updateC` | Update user data |
| GET | `/user/fansSubmit` | Submit fan status |

### VideoCommentsController — base `/videoComments`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/videoComments/treeData` | Video comments tree |
| POST | `/videoComments/addenp` | Add video comment |

### VideoCommontsThumbsController — base `/videoCommontsThumbs`
| Method | Path | Description |
|--------|------|-------------|
| POST | `/videoCommontsThumbs/thumbs` | Like/thumb a comment |

### VideoController — base `/video`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/video/queryVideos` | Query video feed |
| GET | `/video/firstVideo` | First/featured video |
| POST | `/video/saveReport` | Report a video |
| GET | `/video/disLike` | Dislike a video |
| GET | `/video/personMessage` | User personal message |
| GET | `/video/accidentReward` | Accident reward info |
| POST | `/video/saveAccidentComments` | Save accident comments |
| GET | `/video/getAccidentComment` | Get accident comments |
| GET | `/video/accidentVideoWeb` | Accident video web page |

### VideoThumbsController — base `/videoThumbs`
| Method | Path | Description |
|--------|------|-------------|
| POST | `/videoThumbs/thumbs` | Like/thumb a video |

### ViolationController — base `/violation`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/violation/query` | Query traffic violations |
| GET | `/violation/queryUserCar` | Query user's car violations |

### WebSocketController — base `/api/socket`
| Method | Path | Description |
|--------|------|-------------|
| GET/POST | `/api/socket/page` | WebSocket page |
| GET | `/api/socket/sendMsg` | Send WebSocket message |

### WxPayController — base `/wx/pay`
| Method | Path | Description |
|--------|------|-------------|
| POST | `/wx/pay/createOrder` | Create WeChat Pay order |
| POST | `/wx/pay/createCusOrder` | Create custom WeChat Pay order |
| POST | `/wx/pay/createInfoOrder` | Create info-type order |
| GET | `/wx/pay/couponInformation` | Get coupon information |

---

## Service 2: b (B-End Merchant App Server)

**Base context path:** `/bServer`  
**Port:** `8091`  
**Package:** `com.cheji.b`

### AccidentRecordController — base `/accidentRecord`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/accidentRecord/test` | Test endpoint |
| GET | `/accidentRecord/listDetails` | List accident details |
| GET | `/accidentRecord/updateReal` | Update real-time info |
| GET | `/accidentRecord/updateB` | Update B-side info |

### AndroidDownController — base `/android`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/android/checkVersion` | Check Android APK version |
| GET | `/android/checkVersionChedian` | Check car-shop app version |

### AppBeautyPriceDetailController — base `beautyPriceDetail`
| Method | Path | Description |
|--------|------|-------------|
| POST | `beautyPriceDetail/saveBeautyDeatils` | Save beauty service details |
| GET | `beautyPriceDetail/beautyParameter` | Get beauty service parameters |

### AppBUserConfigController — base `/buserConfig`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/buserConfig/getBId` | Get merchant B-user ID config |
| GET | `/buserConfig/readMessage` | Read messages |

### AppCleanIndetController — base `/appleanIndet`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/appleanIndet/indentDetails` | Car wash order details |
| GET | `/appleanIndet/cleanIndentList` | Car wash order list |

### AppRescueIndentController — base `/appRescueIndent`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/appRescueIndent/rescueDetails` | Rescue order details |
| GET | `/appRescueIndent/rescueIndentList` | Rescue order list |
| GET | `/appRescueIndent/goToScene` | Go to rescue scene action |

### AppSendOutSheetController — base `/appSendOutSheet`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/appSendOutSheet/adjustersList` | List adjusters/dispatchers |
| GET | `/appSendOutSheet/adjustersDetails` | Adjuster details |
| POST | `/appSendOutSheet/updateAdjusters` | Update adjuster info |

### AppSprayPaintIndentController — base `/appSprayPaintIndent`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/appSprayPaintIndent/sprayDetails` | Spray paint order details |
| POST | `/appSprayPaintIndent/offerMerchants` | Submit merchant offer |
| POST | `/appSprayPaintIndent/completeService` | Complete spray paint service |
| GET | `/appSprayPaintIndent/sprayPaintIndentList` | Spray paint order list |

### AppSubstituteDrivingIndentController — base `/appSubstituteDrivingIndent`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/appSubstituteDrivingIndent/toStartAddress` | Navigate to start address |
| GET | `/appSubstituteDrivingIndent/toPickCar` | Navigate to pick up car |
| POST | `/appSubstituteDrivingIndent/reachDesignatedPosition` | Reached designated position |
| GET | `/appSubstituteDrivingIndent/subStituteDriIndentCenter` | Driving order center |
| GET | `/appSubstituteDrivingIndent/subStituteDriIndentList` | Driving order list |
| GET | `/appSubstituteDrivingIndent/subDriDetails` | Driving order details |
| GET | `/appSubstituteDrivingIndent/cancelSubstituteIndent` | Cancel driving order |
| GET | `/appSubstituteDrivingIndent/onlineListenSingle` | Listen for online orders |

### AppUserBMessageController — base `/appUserBMessage`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/appUserBMessage/serviceCenter` | Service center info |
| GET | `/appUserBMessage/modifyBussiness` | Modify business info |

### AppYearCheckIndentController — base `/appYearCheckIndent`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/appYearCheckIndent/yearCheckDetails` | Annual inspection order details |
| GET | `/appYearCheckIndent/yearCheckIndentList` | Annual inspection order list |
| GET | `/appYearCheckIndent/confirmCar` | Confirm car received |

### CdImgController — base `/cdImg`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/cdImg/ClaimTeaMessage` | Claim teacher messages |
| GET | `/cdImg/findAccidMessBill` | Find accident message bills |
| GET | `/cdImg/searchCMessageList` | Search dispatch message list |
| GET | `/cdImg/cMessageList2` | Dispatch message list (v2) |
| GET | `/cdImg/cMessageDetails2` | Dispatch message details (v2) |
| GET | `/cdImg/cMessageList` | Dispatch message list |
| GET | `/cdImg/cMessageDetails` | Dispatch message details |
| GET | `/cdImg/indentList22` | Order list (v2) |
| GET | `/cdImg/IndentDetails` | Order details |
| GET | `/cdImg/pushRepairBill` | Push repair bill |
| POST | `/cdImg/saveCarMessage` | Save car message |
| POST | `/cdImg/checkSign` | Check/upload sign-off |
| GET | `/cdImg/findMessageCar` | Find message car |
| POST | `/cdImg/accidentSer` | Accident service |
| GET | `/cdImg/list` | Image list |
| GET | `/cdImg/details` | Image details |

### CdIndentController — base `/cdIndent`
| Method | Path | Description |
|--------|------|-------------|
| POST | `/cdIndent/pickCar` | Pick up car |
| GET | `/cdIndent/insuranceCompany` | Get insurance company list |
| POST | `/cdIndent/updateIndent` | Update order |
| GET | `/cdIndent/vehicleParts` | Get vehicle parts |
| POST | `/cdIndent/addProduct` | Add product |
| POST | `/cdIndent/accessories` | Manage accessories |
| POST | `/cdIndent/indentList` | Get order list |
| POST | `/cdIndent/dataStatis` | Data statistics |
| POST | `/cdIndent/indentDetails` | Order details |
| POST | `/cdIndent/finishIndent` | Finish order |
| POST | `/cdIndent/deletIndent` | Delete order |
| POST | `/cdIndent/lipei` | Claim/compensation action |

### CdOrderController — base `/cdOrder`
| Method | Path | Description |
|--------|------|-------------|
| POST | `/cdOrder/pickCar` | Pick up car |
| POST | `/cdOrder/orderDetails` | Order details |
| POST | `/cdOrder/orderList` | Order list |
| POST | `/cdOrder/updateOrder` | Update order |
| POST | `/cdOrder/deletIndent` | Delete order |

### CdPartsDetailsController — base `cdPartsDetails`
| Method | Path | Description |
|--------|------|-------------|
| POST | `cdPartsDetails/partsDetails` | Parts details |
| POST | `cdPartsDetails/offerDeatils` | Offer details |
| POST | `cdPartsDetails/buyAcc` | Buy accessories |
| POST | `cdPartsDetails/deleParts` | Delete parts |
| POST | `cdPartsDetails/procurement` | Procurement action |
| POST | `cdPartsDetails/saveWorkOrder` | Save work order |
| POST | `cdPartsDetails/workOrDetails` | Work order details |
| GET | `cdPartsDetails/priceDetails` | Price details |

### CdUserController — base `/cdUser`
| Method | Path | Description |
|--------|------|-------------|
| POST | `/cdUser/register` | Register car-shop user |
| POST | `/cdUser/loginForPass` | Login with password |
| POST | `/cdUser/signOut` | Sign out |

### CleanPriceDetailController — base `/cleanPriceDetail`
| Method | Path | Description |
|--------|------|-------------|
| POST | `/cleanPriceDetail/saveCleanDeatils` | Save car wash price details |
| GET | `/cleanPriceDetail/cleanParameter` | Get car wash parameters |

### FeedbackController — base `/feedback`
| Method | Path | Description |
|--------|------|-------------|
| POST | `/feedback/saveFeedback` | Submit feedback |

### IndentController — base `/indent`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/indent/todayEarning` | Today's earnings |
| GET | `/indent/effectiveOrder` | Effective orders |
| GET | `/indent/myIndent` | My orders |
| GET | `/indent/consumptionDeductions` | Consumption deductions |
| GET | `/indent/indentDetails` | Order details |
| GET | `/indent/updateIndent` | Update order status |

### LableDetailsReviewTreeController — base `/lableDetailsReviewTree`
| Method | Path | Description |
|--------|------|-------------|
| POST | `/lableDetailsReviewTree/lableDetails` | Label/tag details |
| GET | `/lableDetailsReviewTree/lableList` | Label list |
| GET | `/lableDetailsReviewTree/projectDetails` | Project/service details |
| GET | `/lableDetailsReviewTree/shelvesService` | Shelf/unshelf service |
| POST | `/lableDetailsReviewTree/addNewSer` | Add new service |
| POST | `/lableDetailsReviewTree/updataWorkPlace` | Update work place info |
| GET | `/lableDetailsReviewTree/findServiceDetail` | Find service details |

### LoginController — base `/user`
| Method | Path | Description |
|--------|------|-------------|
| POST | `/user/getLoginCode` | Get login SMS code |
| POST | `/user/loginForCode` | Login with SMS code |
| POST | `/user/getRegisterCode` | Get registration SMS code |
| POST | `/user/register` | Register merchant user |
| POST | `/user/loginForPass` | Login with password |
| POST | `/user/signOut` | Sign out |
| POST | `/user/logOutCode` | Logout verification code |
| POST | `/user/smsForgetPass` | SMS for forgot password |
| POST | `/user/forgetUpdatePass` | Reset forgotten password |

### MerchantsCommentsTreeController — base `/merchantsCommentsTree`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/merchantsCommentsTree/merchComments` | Merchant comments |
| POST | `/merchantsCommentsTree/replyComment` | Reply to comment |
| GET | `/merchantsCommentsTree/pushIndentComment` | Push order comment notification |

### MerchantsInfoBannerController — base `/merchantsInfoBanner`
| Method | Path | Description |
|--------|------|-------------|
| POST | `/merchantsInfoBanner/addImg` | Add banner image |
| GET | `/merchantsInfoBanner/deletImg` | Delete banner image |

### MerchantsTreeController — base `/merchantsTree`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/merchantsTree/homePage` | Merchant home page data |
| GET | `/merchantsTree/evaluation` | Merchant evaluation |
| GET | `/merchantsTree/mine` | My merchant profile |
| GET | `/merchantsTree/serviceDisplay` | Service display |
| GET | `/merchantsTree/stores` | Store info |
| POST | `/merchantsTree/updateStores` | Update store info |
| GET | `/merchantsTree/updateAddress` | Update store address |
| POST | `/merchantsTree/servicerList` | Service staff list |
| GET | `/merchantsTree/deleteServiers` | Delete service staff |
| POST | `/merchantsTree/updateInsurance` | Update insurance info |
| POST | `/merchantsTree/updateBrand` | Update brand info |
| GET | `/merchantsTree/allBrand` | All car brands |
| GET | `/merchantsTree/allMessage` | All messages |
| GET | `/merchantsTree/targetPrice` | Target pricing |
| GET | `/merchantsTree/updateLocatioin` | Update location |

### PageController — base `/page` (returns HTML views)
| Method | Path | Description |
|--------|------|-------------|
| GET | `/page/agreement` | Agreement page |
| GET | `/page/register` | Registration page |
| GET | `/page/guide` | Guide page |
| GET | `/page/privacy` | Privacy policy |
| GET | `/page/userAgreement` | User agreement |

### PersonalController — base `/personal`
| Method | Path | Description |
|--------|------|-------------|
| GET/POST | `/personal/down` | Download personal data |

### PushBillController — base `/pushBill`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/pushBill/billList` | Push bill list |
| GET | `/pushBill/screenBill` | Screen/filter bills |

### UserController — base `/user`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/user/getBankList` | Get bank card list |
| POST | `/user/addBank` | Add bank card |
| POST | `/user/addCashOut` | Apply for cash-out |
| GET | `/user/bankCardList` | Bank card list |
| GET | `/user/removeBankCard` | Remove bank card |
| GET | `/user/changeList` | Account change list |
| GET | `/user/changeDetails` | Account change details |

### WxPayController — base `/wx/pay`
| Method | Path | Description |
|--------|------|-------------|
| POST | `/wx/pay/createCusOrder` | Create custom WeChat Pay order |
| POST | `/wx/pay/createInfoOrder` | Create info-type order |
| POST | `/wx/pay/notify` | WeChat Pay payment callback |
| POST | `/wx/pay/queryOrderStatus` | Query order payment status |

---

## Service 3: icars-admin (Admin Web Backend)

**Base context path:** `/` (no context path; commented-out `/admin`)  
**Port:** `8082`  
**Package:** `com.stylefeng.guns`

This service is primarily a traditional MVC admin panel (returns HTML views) with some REST endpoints mixed in.

### AccidentController — base `/accid`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/accid` | Accident management home page |
| GET | `/accid/accid_push_claims` | Push claims advisor page |
| GET | `/accid/accid_check_reason` | Check/audit reason selection page |
| GET | `/accid/redpay_choose` | Red packet payment selection page |
| GET | `/accid/accid_push` | Push to 4S store page |
| GET | `/accid/list` | Query accident report list |
| GET | `/accid/redpack/sum` | Total accident red packet amount |
| GET | `/accid/pushFsList` | Get push 4S store list |
| GET | `/accid/pushClaimsList` | Get push claims advisor list |
| GET | `/accid/pushMaintainList` | Get push maintenance employee list |
| POST | `/accid/pushFours` | Push accident to 4S store |
| POST | `/accid/pushClaims` | Push accident to claims advisor |
| POST | `/accid/checkSuccess` | Approve/audit accident report |
| POST | `/accid/push` | Push accident info |

### AlipayActivityController — base `/alipayActivity`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/alipayActivity` | Alipay activity home |
| GET | `/alipayActivity/alipayActivity_add` | Add activity page |
| GET | `/alipayActivity/alipayActivity_update/{id}` | Update activity page |
| GET | `/alipayActivity/list` | Alipay activity list |
| POST | `/alipayActivity/add` | Add Alipay activity |
| POST | `/alipayActivity/delete` | Delete Alipay activity |
| POST | `/alipayActivity/update` | Update Alipay activity |

### BizAccidLevelController — base `/bizAccidLevel`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/bizAccidLevel` | Accident level home |
| GET | `/bizAccidLevel/bizAccidLevel_add` | Add accident level page |
| GET | `/bizAccidLevel/bizAccidLevel_update/{id}` | Update accident level page |
| GET | `/bizAccidLevel/list` | Accident level list |
| POST | `/bizAccidLevel/add` | Add accident level |
| POST | `/bizAccidLevel/delete` | Delete accident level |
| POST | `/bizAccidLevel/update` | Update accident level |
| GET | `/bizAccidLevel/detail/{id}` | Accident level detail |

### BizAlipayBillController — base `/bizAlipayBill`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/bizAlipayBill` | Alipay bill management home |
| GET | `/bizAlipayBill/bizAlipayBill_add` | Add Alipay bill page |
| GET | `/bizAlipayBill/list` | Alipay bill list |
| POST | `/bizAlipayBill/rePay` | Re-initiate Alipay payment |

### BizClaimController — base `/bizClaim`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/bizClaim` | Claims home |
| GET | `/bizClaim/bizClaim_add` | Add claim page |
| GET | `/bizClaim/bizClaim_update/{id}` | Update claim page |
| GET | `/bizClaim/claims_push_claims` | Push to claims advisor page |
| GET | `/bizClaim/list` | Claims list |
| POST | `/bizClaim/add` | Add claim |
| POST | `/bizClaim/delete` | Delete claim |
| POST | `/bizClaim/update` | Update claim |
| GET | `/bizClaim/detail/{id}` | Claim detail |
| POST | `/bizClaim/pushClaims` | Push claim to advisor |

### BizClaimerShowController — base `/bizClaimerShow`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/bizClaimerShow` | Claimer showcase home |
| GET | `/bizClaimerShow/bizClaimerShow_add` | Add claimer show page |
| GET | `/bizClaimerShow/bizClaimerShow_update/{id}` | Update claimer show page |
| GET | `/bizClaimerShow/list` | Claimer showcase list |
| POST | `/bizClaimerShow/add` | Add claimer showcase |
| POST | `/bizClaimerShow/delete` | Delete claimer showcase |
| POST | `/bizClaimerShow/update` | Update claimer showcase |
| GET | `/bizClaimerShow/detail/{id}` | Claimer showcase detail |

### BizInsuranceCompanyController — base `/bizInsuranceCompany`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/bizInsuranceCompany` | Insurance company home |
| GET | `/bizInsuranceCompany/bizInsuranceCompany_add` | Add company page |
| GET | `/bizInsuranceCompany/bizInsuranceCompany_update/{id}` | Update company page |
| GET | `/bizInsuranceCompany/list` | Insurance company list |
| POST | `/bizInsuranceCompany/add` | Add insurance company |
| GET | `/bizInsuranceCompany/listForSelection` | List for selection dropdown |
| POST | `/bizInsuranceCompany/delete` | Delete insurance company |
| POST | `/bizInsuranceCompany/update` | Update insurance company |
| GET | `/bizInsuranceCompany/detail/{id}` | Insurance company detail |

### BizMaintainPackageController — base `/bizMaintainPackage`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/bizMaintainPackage` | Maintenance package home |
| GET | `/bizMaintainPackage/bizMaintainPackage_add` | Add package page |
| GET | `/bizMaintainPackage/bizMaintainPackage_update/{id}` | Update package page |
| GET | `/bizMaintainPackage/list` | Maintenance package list |
| POST | `/bizMaintainPackage/add` | Add maintenance package |
| POST | `/bizMaintainPackage/delete` | Delete package |
| POST | `/bizMaintainPackage/onShowOrOffShow` | Toggle package on/off shelf |
| POST | `/bizMaintainPackage/update` | Update package |
| GET | `/bizMaintainPackage/detail/{id}` | Package detail |

### BizMaintainPackageOrderController — base `/bizMaintainPackageOrder`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/bizMaintainPackageOrder` | Maintenance order home |
| GET | `/bizMaintainPackageOrder/order_push` | Push order page |
| GET | `/bizMaintainPackageOrder/bizMaintainPackageOrder_add` | Add order page |
| GET | `/bizMaintainPackageOrder/bizMaintainPackageOrder_update/{id}` | Update order page |
| GET | `/bizMaintainPackageOrder/list` | Maintenance order list |
| POST | `/bizMaintainPackageOrder/add` | Add maintenance order |
| POST | `/bizMaintainPackageOrder/delete` | Delete order |
| POST | `/bizMaintainPackageOrder/update` | Update order |
| POST | `/bizMaintainPackageOrder/order/accept` | Accept vehicle (confirm arrival) |
| POST | `/bizMaintainPackageOrder/order/cancel` | Cancel order |
| POST | `/bizMaintainPackageOrder/order/finish` | Complete order |
| POST | `/bizMaintainPackageOrder/pushRepaireman` | Push to repair worker |
| GET | `/bizMaintainPackageOrder/detail/{id}` | Order detail |

### BizOpenFeedbackController — base `/bizOpenFeedback`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/bizOpenFeedback` | Open platform feedback home |
| GET | `/bizOpenFeedback/bizOpenFeedback_add` | Add feedback page |
| GET | `/bizOpenFeedback/bizOpenFeedback_update/{id}` | Update feedback page |
| GET | `/bizOpenFeedback/list` | Feedback list |
| POST | `/bizOpenFeedback/add` | Add feedback |
| POST | `/bizOpenFeedback/delete` | Delete feedback |
| POST | `/bizOpenFeedback/update` | Update feedback |
| GET | `/bizOpenFeedback/detail/{id}` | Feedback detail |

### BizOpenNotifyController — base `/bizOpenNotify`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/bizOpenNotify` | Notification home |
| GET | `/bizOpenNotify/bizOpenNotify_add` | Add notification page |
| GET | `/bizOpenNotify/bizOpenNotify_update/{id}` | Update notification page |
| GET | `/bizOpenNotify/list` | Notification list |
| POST | `/bizOpenNotify/add` | Add notification |
| POST | `/bizOpenNotify/delete` | Delete notification |
| POST | `/bizOpenNotify/update` | Update notification |
| GET | `/bizOpenNotify/detail/{id}` | Notification detail |

### BizRepairePackageController — base `/bizRepairePackage`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/bizRepairePackage` | Repair package home |
| GET | `/bizRepairePackage/bizRepairePackage_add` | Add repair package page |
| GET | `/bizRepairePackage/bizRepairePackage_update/{id}` | Update repair package page |
| GET | `/bizRepairePackage/list` | Repair package list |
| POST | `/bizRepairePackage/add` | Add repair package |
| POST | `/bizRepairePackage/delete` | Delete package |
| POST | `/bizRepairePackage/onShowOrOffShow` | Toggle package on/off shelf |
| POST | `/bizRepairePackage/update` | Update package |
| GET | `/bizRepairePackage/detail/{id}` | Package detail |

### BizRepairePackageOrderController — base `/bizRepairePackageOrder`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/bizRepairePackageOrder` | Repair order home |
| GET | `/bizRepairePackageOrder/order_push` | Push repair order page |
| GET | `/bizRepairePackageOrder/bizRepairePackageOrder_add` | Add order page |
| GET | `/bizRepairePackageOrder/bizRepairePackageOrder_update/{id}` | Update order page |
| GET | `/bizRepairePackageOrder/list` | Repair order list |
| POST | `/bizRepairePackageOrder/add` | Add repair order |
| POST | `/bizRepairePackageOrder/delete` | Delete order |
| POST | `/bizRepairePackageOrder/update` | Update order |
| POST | `/bizRepairePackageOrder/order/accept` | Accept vehicle |
| POST | `/bizRepairePackageOrder/order/cancel` | Cancel repair order |
| POST | `/bizRepairePackageOrder/order/finish` | Complete repair order |
| POST | `/bizRepairePackageOrder/pushRepaireman` | Push to repair worker |
| GET | `/bizRepairePackageOrder/detail/{id}` | Repair order detail |

### BizWxSalaryController — base `/bizWxSalary`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/bizWxSalary` | WeChat salary home |
| GET | `/bizWxSalary/bizWxSalary_add` | Add salary record page |
| GET | `/bizWxSalary/bizWxSalary_update/{id}` | Update salary page |
| GET | `/bizWxSalary/list` | Salary list |
| POST | `/bizWxSalary/add` | Add salary record |
| POST | `/bizWxSalary/delete` | Delete salary record |
| POST | `/bizWxSalary/update` | Update salary record |
| GET | `/bizWxSalary/detail/{id}` | Salary detail |

### BizWxpayOrderController — base `/bizWxpayOrder`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/bizWxpayOrder` | WeChat Pay order home |
| GET | `/bizWxpayOrder/bizWxpayOrder_add` | Add order page |
| GET | `/bizWxpayOrder/bizWxpayOrder_update/{id}` | Update order page |
| GET | `/bizWxpayOrder/list` | WeChat Pay order list |
| POST | `/bizWxpayOrder/add` | Add WeChat Pay order |
| POST | `/bizWxpayOrder/delete` | Delete order |
| POST | `/bizWxpayOrder/update` | Update order |
| GET | `/bizWxpayOrder/detail/{id}` | Order detail |

### BizYckCzmxController — base `/bizYckCzmx`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/bizYckCzmx` | Yck transaction home |
| GET | `/bizYckCzmx/bizYckCzmx_add` | Add record page |
| GET | `/bizYckCzmx/bizYckCzmx_update/{id}` | Update record page |
| GET | `/bizYckCzmx/list` | Transaction list |
| POST | `/bizYckCzmx/add` | Add record |
| POST | `/bizYckCzmx/delete` | Delete record |
| POST | `/bizYckCzmx/update` | Update record |
| GET | `/bizYckCzmx/detail/{id}` | Record detail |

### BlackboardController — base `/blackboard`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/blackboard` | Dashboard/overview page |

### CityController — base `/city`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/city` | City management home |
| GET | `/city/city_add` | Add city page |
| GET | `/city/city_update/{id}` | Update city page |
| GET | `/city/list` | City list |
| POST | `/city/add` | Add city |
| POST | `/city/delete` | Delete city |
| POST | `/city/update` | Update city |
| GET | `/city/detail/{id}` | City detail |

### CodeController — base `/code`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/code` | Code generation home |
| POST | `/code/generate` | Generate code |

### DeptController — base `/dept`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/dept` | Department management home |
| GET | `/dept/dept_add` | Add department page |
| GET | `/dept/dept_update/{id}` | Update department page |
| GET | `/dept/tree` | Department tree list |
| POST | `/dept/add` | Add department |
| GET | `/dept/list` | All departments list |
| GET | `/dept/detail/{id}` | Department detail |
| POST | `/dept/update` | Update department |
| POST | `/dept/delete` | Delete department |

### DictController — base `/dict`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/dict` | Dictionary home |
| GET | `/dict/dict_add` | Add dictionary page |
| GET | `/dict/dict_edit/{id}` | Edit dictionary page |
| POST | `/dict/add` | Add dictionary entry |
| GET | `/dict/list` | Dictionary list |
| GET | `/dict/detail/{id}` | Dictionary detail |
| POST | `/dict/update` | Update dictionary |
| POST | `/dict/delete` | Delete dictionary |

### FileUploadController — no base mapping
| Method | Path | Description |
|--------|------|-------------|
| POST | `/file/uploadfile` | Upload file |

### KaptchaController — base `/kaptcha`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/kaptcha` | Generate captcha |
| GET | `/kaptcha/{pictureId}` | Return captcha image |

### LogController — base `/log`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/log` | Operation log home |
| GET | `/log/list` | Operation log list |
| GET | `/log/detail/{id}` | Log detail |
| POST | `/log/delLog` | Clear logs |

### LoginController — no base mapping
| Method | Path | Description |
|--------|------|-------------|
| GET | `/` | Redirect to main page |
| GET | `/login` | Login page |
| POST | `/login` | Perform login |
| GET | `/logout` | Logout |

### LoginLogController — base `/loginLog`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/loginLog` | Login log home |
| GET | `/loginLog/list` | Login log list |
| POST | `/loginLog/delLoginLog` | Clear login logs |

### MenuController — base `/menu`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/menu` | Menu list home |
| GET | `/menu/menu_add` | Add menu page |
| GET | `/menu/menu_edit/{id}` | Edit menu page |
| POST | `/menu/edit` | Edit menu |
| GET | `/menu/list` | Menu list |
| POST | `/menu/add` | Add menu |
| POST | `/menu/remove` | Remove menu |
| GET | `/menu/view/{id}` | View menu |
| GET | `/menu/menuTreeList` | Menu tree (homepage) |
| GET | `/menu/selectMenuTreeList` | Menu tree (parent selection) |
| GET | `/menu/menuTreeListByRoleId/{roleId}` | Menu tree by role |

### NoticeController — base `/notice`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/notice` | Notice list home |
| GET | `/notice/notice_add` | Add notice page |
| GET | `/notice/notice_update/{id}` | Update notice page |
| GET | `/notice/hello` | Home notice |
| GET | `/notice/list` | Notice list |
| POST | `/notice/add` | Add notice |
| POST | `/notice/delete` | Delete notice |
| POST | `/notice/update` | Update notice |

### OpenClaimController — base `/openClaim`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/openClaim/partner` | Partner store permission page |
| GET | `/openClaim` | Open claims home |
| GET | `/openClaim/openClaim_addFixLoss/{id}` | Add fixed-loss amount page |
| GET | `/openClaim/openClaim_addReportEx/{id}` | Add exception report page |
| GET | `/openClaim/openClaim_addPayBillNo/{id}` | Add payment voucher page |
| GET | `/openClaim/openClaim_addPayBillNoForClaim/{id}` | Add payment voucher (claim) |
| GET | `/openClaim/openClaim_update/{id}` | Update claim page |
| GET | `/openClaim/openClaim_detail/{orderNo}` | Claim detail by order |
| GET | `/openClaim/list` | Open claims list |
| GET | `/openClaim/list/export` | Export claims list |
| GET | `/openClaim/partner/list/export` | Export partner claims list |
| GET | `/openClaim/partner/list` | Partner store order list |
| GET | `/openClaim/partner/fixLossSum` | Partner fixed-loss total |
| GET | `/openClaim/fixLossSum` | Group fixed-loss total |

### PushRecordController — base `/pushRecord`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/pushRecord` | Push record home |
| GET | `/pushRecord/fsMgr` | 4S store query page |
| GET | `/pushRecord/list` | Push record list |
| GET | `/pushRecord/pushFS/list` | Push 4S store record list |
| POST | `/pushRecord/delete` | Delete push record |
| POST | `/pushRecord/changeStatus` | Set push record as viewed |

### RoleController — base `/role`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/role` | Role list home |
| GET | `/role/role_add` | Add role page |
| GET | `/role/role_edit/{id}` | Edit role page |
| GET | `/role/role_assign/{id}` | Role assignment page |
| GET | `/role/list` | Role list |
| POST | `/role/add` | Add role |
| POST | `/role/edit` | Edit role |
| POST | `/role/remove` | Delete role |
| GET | `/role/view/{id}` | View role |
| POST | `/role/setAuthority` | Set role permissions |
| GET | `/role/roleTreeList` | Role tree list |
| GET | `/role/roleTreeListByUserId/{userId}` | Role tree by user |

### TestController — base `/api/test`
| Method | Path | Description |
|--------|------|-------------|
| GET/POST | `/api/test` | Test home |
| GET/POST | `/api/test/delete` | Test delete |

### TxMapController — base `/tx/map`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/tx/map` | Tencent map home |
| GET | `/tx/map/addAdjustersLocation` | Add adjuster location |
| GET | `/tx/map/adjustersLocation` | Get adjuster locations |

### UserMgrController — base `/mgr`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/mgr` | Admin user list home |
| GET | `/mgr/user_add_location` | Add admin location page |
| GET | `/mgr/user_add` | Add admin user page |
| GET | `/mgr/toQrPage/{account}` | Promotion QR code page |
| GET | `/mgr/user_qrCode/{account}` | Get promotion QR code image |
| GET | `/mgr/role_assign/{userId}` | Role assignment page |
| GET | `/mgr/user_edit/{userId}` | Edit admin user page |
| GET | `/mgr/user_info` | View admin user detail |
| GET | `/mgr/user_chpwd` | Change password page |
| POST | `/mgr/changePwd` | Change current user's password |
| GET | `/mgr/list` | Admin user list |
| POST | `/mgr/add` | Add admin user |
| POST | `/mgr/edit` | Edit admin user |
| POST | `/mgr/delete` | Delete admin user |

### WxAuthController — no base mapping (REST)
| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/v1/wx/getSession` | Get WeChat mini-app session |
| GET | `/api/v1/wx/testRedis` | Test Redis connection |
| POST | `/api/v1/wx/decodeUserInfo` | Decode WeChat user info |
| GET | `/api/v1/wx/verify` | WeChat token verification (GET) |
| POST | `/api/v1/wx/getGzhTokenAndOpenId` | Get GZH token and OpenID |
| POST | `/api/v1/wx/verify` | WeChat token verification (POST) |
| POST | `/api/v1/wx/createMenu` | Create WeChat menu |
| POST | `/api/v1/wx/getMenu` | Get WeChat menu |

### WxPayNotifyController — no base mapping (REST)
| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/wxpay/notify` | WeChat Pay payment result callback |

### WxpayBillController — base `/wxpayBill`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/wxpayBill` | WeChat Pay bill home |
| GET | `/wxpayBill/wxpayBill_add` | Add WeChat Pay bill page |
| GET | `/wxpayBill/list` | WeChat Pay bill list |
| POST | `/wxpayBill/rePay` | Re-initiate WeChat payment |

### WxRestController — no base mapping (REST)
| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/wx/accid/add` | Submit accident report (WeChat user) |
| POST | `/api/v1/wx/accid/saveOpenId` | Save WeChat OpenID |
| POST | `/api/v1/wx/accid/selectOpenId` | Query WeChat OpenID |
| POST | `/api/v1/wx/accid/newAdd` | Submit new accident report |
| PUT | `/api/v1/wx/accid/check` | Update accident audit status |
| POST | `/api/v1/wx/alipay/add` | Bind Alipay account |
| POST | `/api/v1/wx/extension/add` | Bind promoter/extension account |
| POST | `/api/v1/wx/userinfo/bind` | Store WeChat user avatar/nickname |
| GET | `/api/v1/wx/user/user_qrCode/{key}` | Get promotion QR code |
| GET | `/api/v1/wx/user/get` | Query WeChat user info |
| GET | `/api/v1/wx/accids/get` | Query user's accident records |
| GET | `/api/v1/wx/redPay/get` | Query red packet disbursement records |
| GET | `/api/v1/wx/accids/pushRecord/updateStatus` | Update accident push record status |

### XcxMaintenanceController — base `/xcxMaintenance`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/xcxMaintenance` | Mini-app maintenance home |
| GET | `/xcxMaintenance/xcxMaintenance_add` | Add maintenance page |
| GET | `/xcxMaintenance/xcxMaintenance_update/{id}` | Update maintenance page |
| GET | `/xcxMaintenance/list` | Maintenance list |
| POST | `/xcxMaintenance/add` | Add maintenance record |
| POST | `/xcxMaintenance/delete` | Delete maintenance record |
| POST | `/xcxMaintenance/update` | Update maintenance record |
| GET | `/xcxMaintenance/detail/{id}` | Maintenance detail |

### AppController (app pages) — no base mapping
| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/v1/app/goAppNews` | App news page |
| GET | `/api/v1/app/goAppHome` | App home page |
| GET | `/api/v1/app/privacy` | App privacy policy page |

### AppRestController — no base mapping (REST)
| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/app/getAccList` | Get accident record list (app) |
| POST | `/api/v1/app/getAccListDetail` | Get accident record detail (app) |
| POST | `/api/v1/app/updateAccStatus` | Update accident status (claimer) |
| POST | `/api/v1/app/claimer/orderadd` | Claimer submits claim order |
| POST | `/api/v1/app/claimer/order/supply` | Claimer supplements claim info |
| POST | `/api/v1/app/forgetPassword` | Forgot password |
| POST | `/api/v1/app/changePassword` | Change password |
| POST | `/api/v1/app/updateUserDetail` | Update user profile |
| POST | `/api/v1/app/getUserDetail` | Get user profile |
| POST | `/api/v1/app/getClaimUserDetail` | Get claim user profile |
| POST | `/api/v1/app/claimerShowget` | Showcase claims advisor list |

### ClaimUserController — base `/claimUser`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/claimUser` | Claims advisor statistics home |
| GET | `/claimUser/list` | Claims advisor statistics list |

### WxCouponCountController — base `/couponCount`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/couponCount` | Red packet statistics home |
| GET | `/couponCount/list` | Red packet statistics list |

### XcxUserCountController — base `/xcxUserCount`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/xcxUserCount` | Mini-app new user statistics home |
| GET | `/xcxUserCount/list` | User statistics list |

### XcxController — no base mapping (mini-app REST)
| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/xcx/home/newsList` | Mini-app news list |
| GET | `/api/xcx/home/video` | Mini-app home video |
| GET | `/api/xcx/home/setVideo` | Set home video |
| GET | `/api/xcx/maintainList` | Maintenance list |
| GET | `/api/xcx/maintainInfo` | Maintenance info detail |
| POST | `/api/xcx/wx/xcx/claim/add` | Submit claim (mini-app) |
| POST | `/api/xcx/wx/xcx/myClaim` | My claims (mini-app) |
| POST | `/api/xcx/personal` | Personal info (mini-app) |
| GET | `/api/xcx/getSession` | Get mini-app session |
| GET | `/api/xcx/getThumbnail` | Get thumbnail |

---

## Service 4: icars-rest (REST API Server)

**Base context path:** `/` (no context path)  
**Port:** `8443` (HTTPS)  
**Package:** `com.stylefeng.guns.rest`

### AuthController — no base mapping
| Method | Path | Description |
|--------|------|-------------|
| POST | `/${jwt.auth-path}` (default: `/auth`) | JWT authentication — exchange credentials for token |

### ExampleController — base `/hello`
| Method | Path | Description |
|--------|------|-------------|
| GET/POST | `/hello` | Hello world example |

---

## Service 5: jeesite_new (JeeSite Admin Backend)

**Base context path:** `/jeesite` (production); `/js` (local dev)  
**Port:** `9002`  
**Package:** `com.jeesite.modules`  
**Note:** `${adminPath}` resolves to `/a` (the JeeSite framework default; commented in config). All URLs below use `/a` as the adminPath prefix.

### AppAccidentRecordController — base `/a/app/appAccidentRecord`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` or `...` | Query accident record list |
| GET | `.../listData` | Accident record list data (JSON) |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save accident record |
| GET | `.../appAccidentTwoList` | Second accident list view |
| GET | `.../listDataTwo` | Second accident list data |
| GET | `.../formtwo` | Second form |
| GET | `.../failure` | Audit failure action |
| GET | `.../showApp` | Show app data |
| GET | `.../getBrand4Redis2` | Get brand cache (v2) |
| GET | `.../getBrand4Redis` | Get brand cache |
| GET | `.../searchBrand` | Search brand |
| GET | `.../addScore` | Increment brand score in Redis |
| GET | `.../addGarage` | Add garage |
| GET | `.../addMer` | Add merchant |
| GET | `.../addClia` | Add claims advisor |
| GET | `.../find4sStore2` | Find 4S store (v2) |
| GET | `.../find4sStore` | Find nearest 4S stores |
| GET | `.../pushBill` | Push billing info |
| GET | `.../pushBill2` | Push billing info (v2) |
| GET | `.../black` | Blacklist action |
| GET | `.../green` | Greenlist action |
| GET | `.../accidentTag` | Accident tagging |

### AppAllMerchantsController — base `/a/app/appAllMerchants`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | All merchants list |
| GET | `.../listData` | All merchants list data |

### AppAuctionBailLogController — base `/a/app/appAuctionBailLog`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Bail payment log list |
| GET | `.../listData` | Bail log data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save bail log |

### AppAuctionBidLogController — base `/a/app/appAuctionBidLog`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Bid log list |
| GET | `.../listData` | Bid log data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save bid log |
| GET | `.../delete` | Delete bid log |

### AppAuctionController — base `/a/app/appAuction`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Auction business list |
| GET | `.../listData` | Auction data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save auction record |
| GET | `.../delete` | Delete auction record |

### AppAuctionMessageIdentifyController — base `/a/app/appAuctionMessageIdentify`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Authentication info list |
| GET | `.../listData` | Authentication data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save authentication info |
| GET | `.../delete` | Delete authentication info |

### AppAuctionOnePriceCarLogController — base `/a/app/appAuctionOnePriceCarLog`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Fixed-price payment log list |
| GET | `.../listData` | Payment log data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save payment log |
| GET | `.../delete` | Delete payment log |

### AppAuctionOrderController — base `/a/app/appAuctionOrder`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Auction order list |
| GET | `.../listData` | Auction order data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save auction order |

### AppAuctionPayLogController — base `/a/app/appAuctionPayLog`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | VIP payment log list |
| GET | `.../listData` | Payment log data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save payment log |
| GET | `.../delete` | Delete payment log |

### AppAuctionUpController — base `/a/app/appAuctionUp`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Auction car listing list |
| GET | `.../checkAppAuctionState` | Check auction state |
| GET | `.../listData` | Listing data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save car listing |
| GET | `.../saveEndTime` | Save end time |
| GET | `.../uploadReplace` | Upload replacement images |

### AppBUserController — base `/a/app/appBUser`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | B-user (merchant) list |
| GET | `.../listData` | B-user data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save B-user |
| GET | `.../addgeo` | Add merchant to geo index |
| GET | `.../updateState` | Update user state |
| GET | `.../passAccount` | Approve account |
| GET | `.../banLogin` | Ban login |
| GET | `.../test` | Test action |
| GET | `.../addRescueMerchants` | Add rescue merchant |
| GET | `.../findGeoMerchants` | Find geo merchants |
| GET | `.../delRescueMerchants` | Delete rescue merchant |

### AppBusinessConfirmController — base `/a/app/appBusinessConfirm`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Business confirmation list |
| GET | `.../listData` | List data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save business confirmation |
| GET | `.../delete` | Delete record |
| GET | `.../addBusinessList` | Add business list |
| GET | `.../replaceImg` | Replace image |
| GET | `.../addAddDayList` | Add daily list |
| POST | `.../saveBusinessCon` | Save business config |

### AppCleanIndetController — base `/a/app/appCleanIndet`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Car wash order list |
| GET | `.../listData` | Car wash order data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save car wash order |
| GET | `.../disable` | Disable order |
| GET | `.../enable` | Enable order |
| GET | `.../delete` | Delete order |

### AppCleanPriceDetailController — base `/a/app/appCleanPriceDetail`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Cleaning price detail list |
| GET | `.../listData` | Price detail data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save price detail |

### AppEveryMesgController — base `/a/app/appEveryMesg`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Daily data message list |
| GET | `.../listData` | Daily data |
| GET | `.../form` | Edit form |

### AppFeedbackController — base `/a/app/appFeedback`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Feedback list |
| GET | `.../listData` | Feedback data |
| GET | `.../form` | Edit form |

### AppIndentController — base `/a/app/appIndent`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Order list |
| GET | `.../mess` | Order messages |
| GET | `.../listData111` | Order list data (old) |
| GET | `.../listData` | Order list data |
| GET | `.../professionalListData` | Professional order list data |
| GET | `.../professionalWorkList` | Professional work list |
| GET | `.../fincAllFixuser` | Find all fix users |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save order |
| GET | `.../delete` | Delete order |
| GET | `.../settlement` | Settlement action |
| GET | `.../countData` | Count/statistics |
| GET | `.../updateNoCome` | Update no-show status |
| GET | `.../export` | Export orders |
| GET | `.../addPeople` | Add people to order |
| GET | `.../findIsCompany` | Find if company |
| GET | `.../removeCompany` | Remove company from order |
| GET | `.../addisCompany` | Add as company |
| GET | `.../addMore` | Add more to order |
| GET | `.../findClaimsTeacher` | Find claims teacher |
| POST | `.../getClaimsTeacher` | Get claims teacher |
| GET | `.../trys` | Test/try action |
| GET | `.../findIndentImg` | Find order images |
| GET | `.../recIndent` | Recover order |

### AppLableController — base `/a/app/appLable`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Label list |
| GET | `.../listData` | Label data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save label |
| GET | `.../delete` | Delete label |

### AppLableDetailsReviewTreeController — base `/a/app/appLableDetailsReviewTree`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Label details tree list |
| GET | `.../listData` | List data |
| GET | `.../form` | Edit form |
| GET | `.../createNextNode` | Create next tree node |
| POST | `.../save` | Save label detail |
| GET | `.../delete` | Delete label detail |
| GET | `.../treeData` | Tree structure data |
| GET | `.../fixTreeData` | Fix tree data structure |
| GET | `.../pass` | Approve/pass item |
| GET | `.../failure` | Reject/fail item |

### AppLeagueController — base `/a/app/appLeague`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Franchise application list |
| GET | `.../listData` | List data |
| GET | `.../form` | Edit form |

### AppMerchantsCommentsController — base `/a/app/appMerchantsComments`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Merchant comment list |
| GET | `.../listData` | Comment data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save comment |
| GET | `.../delete` | Delete comment |

### AppMerchantsCommentsTreeController — base `/a/app/appMerchantsCommentsTree`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Comment tree list |
| GET | `.../listData` | Comment tree data |
| GET | `.../form` | Edit form |
| GET | `.../createNextNode` | Create next node |
| POST | `.../save` | Save comment tree node |
| GET | `.../delete` | Delete comment tree node |
| GET | `.../treeData` | Tree structure data |
| GET | `.../fixTreeData` | Fix tree data |

### AppMerchantsController — base `/a/app/appMerchants`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Merchant list |
| GET | `.../listData` | Merchant data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save merchant |
| GET | `.../delete` | Delete merchant |

### AppMerchantsInfoBannerController — base `/a/app/appMerchantsInfoBanner`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Banner image list |
| GET | `.../listData` | Banner data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save banner |
| GET | `.../delete` | Delete banner |

### AppMerchantsLableController — base `/a/app/appMerchantsLable`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Merchant-label relation list |
| GET | `.../listData` | Relation data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save merchant-label relation |
| GET | `.../delete` | Delete relation |

### AppOrderRollBackController — base `/a/app/appOrderRollBack`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Inventory rollback list |
| GET | `.../listData` | Rollback data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save rollback record |

### AppOurIndentAmountController — base `/a/app/appOurIndentAmount`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Order commission record list |
| GET | `.../listData` | Commission data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save commission record |
| GET | `.../delete` | Delete commission record |

### AppPayAmountRecordController — base `/a/app/appPayAmountRecord`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Red packet amount record list |
| GET | `.../listData` | Amount record data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save amount record |
| GET | `.../payReward` | Pay reward action |
| GET | `.../addPayAount` | Add payment amount |

### AppPushBillController — base `/a/app/appPushBill`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | User deduction record list |
| GET | `.../listData` | Deduction data |
| GET | `.../form` | Edit form |

### AppRescueIndentController — base `/a/app/appRescueIndent`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Rescue order list |
| GET | `.../listData` | Rescue data |
| GET | `.../form` | Edit form |
| GET | `.../backMoney` | Refund rescue order |
| POST | `.../save` | Save rescue order |
| GET | `.../delete` | Delete rescue order |

### AppSendOutSheetController — base `/a/app/appSendOutSheet`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Dispatch sheet list |
| GET | `.../listData` | Dispatch data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save dispatch sheet |
| GET | `.../delete` | Delete dispatch sheet |
| GET | `.../sendSinglePage` | Dispatch single page |
| GET | `.../addSend` | Add send action |
| POST | `.../claimAdjusters` | Claim adjusters |
| POST | `.../findTrack` | Find tracking info |
| GET | `.../sendSingle` | Send single order |
| GET | `.../findClaimAdjusters` | Find claim adjusters |
| GET | `.../addSendOutSheet` | Add send-out sheet |
| GET | `.../upload` | Upload attachment |
| GET | `.../findSendOutTrack` | Find send-out track |
| GET | `.../findSendOutImg` | Find send-out images |

### AppSendRepairController — base `/a/app/appSendRepair`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Accident vehicle repair list |
| GET | `.../listData` | Repair data |
| GET | `.../form` | Edit form |
| POST | `.../save2` | Save repair (v2) |
| POST | `.../save` | Save repair record |
| GET | `.../delete` | Delete repair record |

### AppSprayPaintIndentController — base `/a/app/appSprayPaintIndent`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Spray paint order list |
| GET | `.../listData` | Spray paint data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save spray paint order |
| GET | `.../findImg` | Find spray paint images |

### AppSubstituteDrivingIndentController — base `/a/app/appSubstituteDrivingIndent`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Designated driving order list |
| GET | `.../listData` | Driving order data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save driving order |
| GET | `.../delete` | Delete driving order |
| GET | `.../substituteDirveMerchants` | Substitute driving merchants |
| GET | `.../findGeoSubMerchants` | Find geo-based substitute merchants |
| GET | `.../delSubMerchants` | Delete substitute merchant |

### AppUpMerchantsController — base `/a/app/appUpMerchants`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Image-uploaded 4S store list |
| GET | `.../listData` | 4S store data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save 4S store record |
| GET | `.../delete` | Delete 4S store record |
| GET | `.../sendMessage` | Send message to store |

### AppUserAccountRecordController — base `/a/app/appUserAccountRecord`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | User account record list |
| GET | `.../listData` | Account data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save account record |

### AppUserBehaviorController — base `/a/app/appUserBehavior`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | User video behavior list |
| GET | `.../listData` | Behavior data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save behavior record |

### AppUserBMessageController — base `/a/app/appUserBMessage`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | User service message list |
| GET | `.../listData` | Message data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save service message |
| GET | `.../delete` | Delete message |

### AppUserController — base `/a/app/appUser`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | C-user list |
| GET | `.../listData` | C-user data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save user |
| GET | `.../delete` | Delete user |
| POST | `.../updatePass` | Reset user password |
| POST | `.../addInner` | Add internal user |
| GET | `.../black` | Blacklist user |

### AppUserPlusController — base `/a/app/appUserPlus`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Plus member list |
| GET | `.../listData` | Plus member data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save plus member |

### AppVersionController — base `/a/app/appVersion`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | App version list |
| GET | `.../listData` | Version data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save app version |

### AppVideoCommentsController — base `/a/app/appVideoComments`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Video comment tree list |
| GET | `.../listData` | Comment data |
| GET | `.../form` | Edit form |
| GET | `.../createNextNode` | Create next comment node |
| POST | `.../save` | Save video comment |
| GET | `.../delete` | Delete video comment |
| GET | `.../treeData` | Comment tree data |
| GET | `.../fixTreeData` | Fix comment tree structure |

### AppVideoController — base `/a/app/appVideo`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Video list |
| GET | `.../listData` | Video data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save video |
| GET | `.../upvideo` | Set as homepage video |
| GET | `.../delete` | Delete video |

### AppWxBankController — base `/a/app/appWxBank`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | WeChat bank code list |
| GET | `.../listData` | Bank code data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save bank code |
| GET | `.../delete` | Delete bank code |

### AppWxCashOutRecordController — base `/a/app/appWxCashOutRecord`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Cash-out record list |
| GET | `.../listData` | Cash-out data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save cash-out record |
| GET | `.../delete` | Delete cash-out record |

### AppWxpayOrderController — base `/a/app/appWxpayOrder`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | WeChat Pay order list |
| GET | `.../listData` | Order data |
| GET | `.../form` | Edit form |
| GET | `.../backMoney` | Refund WeChat Pay order |
| POST | `.../save` | Save order |

### AppYearCheckIndentController — base `/a/app/appYearCheckIndent`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Annual inspection order list |
| GET | `.../listData` | Inspection data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save inspection order |
| GET | `.../delete` | Delete inspection order |
| GET | `.../yearCheckMerchants` | Year check merchant management |
| GET | `.../addYearCheckMerchants` | Add year check merchant |
| GET | `.../findYearCheckMerchants` | Find year check merchants |
| GET | `.../delYearCheckMerchants` | Delete year check merchant |
| GET | `.../findImg` | Find inspection images |

### BizAccidentController — base `/a/app/bizAccident`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Accident report list |
| GET | `.../listData` | Accident data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save accident report |
| GET | `.../delete` | Delete accident report |
| GET | `.../addAppform` | Add form from app submission |

### CarInforController — base `/a/app/carInfor`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Car info list |
| GET | `.../listData` | Car data |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save car info |
| GET | `.../delete` | Delete car info |

### CommonsConfigController — base `/a/app/commons`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../configView` | View basic config |
| POST | `.../save` | Save basic config |

### FowardContoller — base `/a/app/forward`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../test` | Test forward |
| GET | `.../findVideo` | Find/proxy video |
| GET | `.../justJudge` | Judge/check action |
| GET | `.../updateRepear` | Update repair info |

### PrivateController — base `/web`
| Method | Path | Description |
|--------|------|-------------|
| GET | `/web/page` | Page view |
| GET | `/web/downApp` | App download page |
| POST | `/web/getCode` | Get verification code |
| POST | `/web/login` | Private login |
| POST | `/web/ops` | Ops/operations endpoint |
| GET | `/web/backMoneyNotify` | Refund result notification callback |

### WebSocketController — base `/api/socket`
| Method | Path | Description |
|--------|------|-------------|
| GET/POST | `/api/socket/page` | WebSocket page |
| GET | `/api/socket/sendMsg` | Send WebSocket message |

### DemoController — base `/a/demo`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../dataGrid/{viewName}` | Demo data grid view |
| GET | `.../form/{viewName}` | Demo form view |

### TestDataController — base `/a/test/testData`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Test data list |
| GET | `.../listData` | Test data JSON |
| GET | `.../form` | Edit form |
| POST | `.../save` | Save test data |
| GET | `.../disable` | Disable test data |
| GET | `.../enable` | Enable test data |
| GET | `.../delete` | Delete test data |

### TestTreeController — base `/a/test/testTree`
| Method | Path | Description |
|--------|------|-------------|
| GET | `.../list` | Test tree list |
| GET | `.../listData` | Test tree data |
| GET | `.../form` | Edit form |
| GET | `.../createNextNode` | Create next tree node |
| POST | `.../save` | Save tree node |
| GET | `.../disable` | Disable node |
| GET | `.../enable` | Enable node |
| GET | `.../delete` | Delete node |
| GET | `.../treeData` | Tree structure data |
| GET | `.../fixTreeData` | Fix tree data |

---

## Summary

| Service | Port | Context Path | Controller Count | Approximate Endpoint Count |
|---------|------|-------------|-------------------|---------------------------|
| **web0508** (C-end App) | 8081 | `/cServer` | 40 | ~160 |
| **b** (B-end Merchant App) | 8091 | `/bServer` | 31 | ~120 |
| **icars-admin** (Admin Panel) | 8082 | `/` (none) | 43 | ~200 |
| **icars-rest** (REST API) | 8443 (HTTPS) | `/` (none) | 2 | ~2 |
| **jeesite_new** (JeeSite Admin) | 9002 | `/jeesite` (prod) | 53 | ~300 |

**Key observations:**
- `web0508` and `b` services share many controller names but serve different audiences: `web0508` is the C-end user app backend, `b` is the B-end merchant app backend.
- `icars-admin` uses a mix of traditional MVC (HTML views, `@Controller`) and REST (`@RestController`) endpoints. The REST endpoints under `/api/v1/wx/...` and `/api/v1/app/...` serve the WeChat mini-program and the claims-adjuster mobile app.
- `icars-rest` is a minimal JWT-secured REST service — only 2 controllers found, one for auth token issuance.
- `jeesite_new` is a JeeSite 4.x-based admin panel where `${adminPath}` resolves to `/a`. All admin module paths follow the pattern `/jeesite/a/app/{module}/{action}`. The `PrivateController` at `/web` and `WebSocketController` at `/api/socket` sit outside the admin path.