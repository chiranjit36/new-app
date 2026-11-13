package com.example.data

import com.example.model.CommerceCourse
import com.example.model.LessonItem
import com.example.model.QuizQuestion
import com.example.model.StudyMaterialItem
import com.example.model.SubscriptionPlanOption
import com.example.model.TeacherProfile

object CatalogData {

    val categories = listOf(
        "All",
        "Accounting",
        "Business Studies",
        "Economics",
        "Finance",
        "Taxation",
        "CA Foundation",
        "CS Foundation",
        "Stock Market Basics"
    )

    val teachers = listOf(
        TeacherProfile(
            id = "teacher_raghav",
            name = "CA Raghav Mehta",
            credentials = "FCA, DISA (ICAI), B.Com (Hons)",
            specialty = "Accounting, Taxation & CA Foundation",
            experienceYears = 14,
            rating = 4.9,
            studentsTaught = "48,000+",
            bio = "Former Big-4 Audit & Direct Tax Manager mentoring All-India Rankers in CA Foundation and Ind AS reporting for over 14 years."
        ),
        TeacherProfile(
            id = "teacher_ananya",
            name = "Dr. Ananya Iyer",
            credentials = "Ph.D. Management (IIM-B), MBA",
            specialty = "Business Studies & Corporate Strategy",
            experienceYears = 11,
            rating = 4.8,
            studentsTaught = "31,500+",
            bio = "Case-study educator specializing in organizational strategy, marketing frameworks, and Board/UG commerce excellence."
        ),
        TeacherProfile(
            id = "teacher_vikram",
            name = "Prof. Vikramaditya Rao",
            credentials = "M.A. Economics (DSE), NET-JRF",
            specialty = "Macroeconomics, Monetary Policy & Trade",
            experienceYears = 16,
            rating = 4.9,
            studentsTaught = "54,200+",
            bio = "Renowned economics author simplifying fiscal multipliers, IS-LM curves, and RBI policy dynamics with real-world charts."
        ),
        TeacherProfile(
            id = "teacher_meera",
            name = "CFA Meera Krishnan",
            credentials = "CFA Charterholder, FRM, M.Fin",
            specialty = "Corporate Finance & Stock Market Basics",
            experienceYears = 9,
            rating = 4.9,
            studentsTaught = "39,800+",
            bio = "Ex-equity research analyst teaching DCF valuation, financial statement analysis, and disciplined retail wealth creation."
        ),
        TeacherProfile(
            id = "teacher_siddharth",
            name = "CS Siddharth Kapoor",
            credentials = "FCS, LL.B., Insolvency Professional",
            specialty = "CS Foundation & Corporate Law",
            experienceYears = 12,
            rating = 4.8,
            studentsTaught = "27,400+",
            bio = "Practicing Company Secretary breaking down Companies Act 2013, secretarial standards, and board governance."
        )
    )

    val subscriptionPlans = listOf(
        SubscriptionPlanOption(
            id = "free",
            name = "Starter Scholar",
            priceInr = 0,
            billingCycle = "Free Forever",
            badge = "BASIC",
            perks = listOf(
                "Access free preview classes & study notes",
                "5 daily AI Doubt Solver prompts",
                "Track attendance & personal streaks"
            )
        ),
        SubscriptionPlanOption(
            id = "pro_monthly",
            name = "Commerce Pro",
            priceInr = 999,
            billingCycle = "per month",
            badge = "POPULAR",
            perks = listOf(
                "Unlimited live + recorded class access",
                "Unlimited Gemini AI Doubt Solver with photo upload",
                "Downloadable formula sheets & mock test series",
                "Verified digital certificates on completion"
            )
        ),
        SubscriptionPlanOption(
            id = "all_access_annual",
            name = "CA/CS All-Access Pass",
            priceInr = 7999,
            billingCycle = "per year (Save 33%)",
            badge = "BEST VALUE",
            perks = listOf(
                "All 8 commerce tracks + CA/CS Foundation bootcamps",
                "1-on-1 faculty doubt clinics & mentorship",
                "Personalized AI weekly study planner",
                "Priority placement & internship prep workshops"
            )
        )
    )

    val curatedCourses: List<CommerceCourse> = listOf(
        CommerceCourse(
            id = "course_accounting_mastery",
            title = "Financial Accounting & IFRS Mastery",
            category = "Accounting",
            description = "Master double-entry bookkeeping, trial balances, depreciation, bank reconciliation, and Ind AS / IFRS financial statements from scratch.",
            teacherId = "teacher_raghav",
            teacherName = "CA Raghav Mehta",
            teacherCredentials = "FCA, DISA (ICAI)",
            priceInr = 1999,
            originalPriceInr = 3499,
            durationHours = 42,
            level = "Foundation",
            ratingAvg = 4.9,
            enrolledCount = 12480,
            nextLiveSession = "LIVE NOW • Ind AS 115 Revenue Workshop",
            isLiveNow = true,
            syllabusHighlights = listOf(
                "Golden Rules & Journalizing Complex Entries",
                "Bank Reconciliation & Rectification of Errors",
                "P&L Account, Balance Sheet & Cash Flow Statement",
                "Ind AS & IFRS Convergence Case Studies"
            ),
            lessons = listOf(
                LessonItem(
                    id = "acc_l1",
                    title = "Live Workshop: Revenue Recognition (Ind AS 115)",
                    durationMinutes = 65,
                    isLive = true,
                    scheduleLabel = "Live Now • Room A-101",
                    summary = "Interactive walkthrough of the 5-step revenue model with real software & retail contracts.",
                    keyTakeaways = listOf(
                        "Identify distinct performance obligations in bundled contracts",
                        "Allocate transaction price using standalone selling prices",
                        "Distinguish point-in-time vs over-time recognition"
                    )
                ),
                LessonItem(
                    id = "acc_l2",
                    title = "Double-Entry Mechanics & Trial Balance",
                    durationMinutes = 48,
                    isLive = false,
                    scheduleLabel = "Recorded • HD 1080p",
                    summary = "Build error-free ledgers and reconcile suspense accounts systematically.",
                    keyTakeaways = listOf(
                        "Assets + Expenses = Liabilities + Equity + Revenue",
                        "Detect one-sided vs two-sided compensating errors"
                    )
                ),
                LessonItem(
                    id = "acc_l3",
                    title = "Cash Flow Statement (Indirect Method)",
                    durationMinutes = 55,
                    isLive = false,
                    scheduleLabel = "Recorded • HD 1080p",
                    summary = "Convert accrual net profit into Operating, Investing, and Financing cash flows.",
                    keyTakeaways = listOf(
                        "Adjust non-cash items like depreciation and amortization",
                        "Compute working capital movements accurately"
                    )
                )
            ),
            studyMaterials = listOf(
                StudyMaterialItem(
                    id = "acc_m1",
                    title = "Accounting Ratios & Balance Sheet Cheat Sheet",
                    format = "PDF Guide",
                    sizeLabel = "1.8 MB",
                    contentPreview = "1. Current Ratio = Current Assets / Current Liabilities (Ideal 2:1)\n2. Quick Ratio = (Current Assets - Inventory - Prepaid) / Current Liabilities\n3. Debt-to-Equity = Total Debt / Shareholders' Equity\n4. ROE = Net Income / Average Shareholders' Equity\n5. Working Capital = Current Assets - Current Liabilities"
                ),
                StudyMaterialItem(
                    id = "acc_m2",
                    title = "50 Solved Journal & Ledger Practice Problems",
                    format = "Workbook",
                    sizeLabel = "3.2 MB",
                    contentPreview = "Includes comprehensive adjustments for outstanding expenses, prepaid insurance, accrued income, bad debts provision, and depreciation under SLM & WDV methods."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    id = "acc_q1",
                    prompt = "Under the accrual basis of accounting, revenue is recognized when:",
                    options = listOf(
                        "Cash is physically deposited in the bank",
                        "Performance obligation is satisfied and earned",
                        "The customer places a verbal inquiry",
                        "At the end of the fiscal year only"
                    ),
                    correctIndex = 1,
                    explanation = "Accrual accounting recognizes revenue when earned (control transfers), regardless of cash receipt timing."
                ),
                QuizQuestion(
                    id = "acc_q2",
                    prompt = "Which item appears under Financing Activities in a Cash Flow Statement?",
                    options = listOf(
                        "Purchase of factory machinery",
                        "Cash collected from trade receivables",
                        "Repayment of long-term bank debentures",
                        "Payment of supplier invoices"
                    ),
                    correctIndex = 2,
                    explanation = "Financing activities reflect changes in equity and long-term borrowings, including debenture repayments."
                ),
                QuizQuestion(
                    id = "acc_q3",
                    prompt = "A credit balance in the Bank Pass Book represents:",
                    options = listOf(
                        "Bank overdraft",
                        "Favorable cash balance at bank",
                        "Dishonored cheque",
                        "Unrecorded bank charges"
                    ),
                    correctIndex = 1,
                    explanation = "From the bank's perspective, a customer's deposit is a liability, hence a credit balance."
                )
            )
        ),
        CommerceCourse(
            id = "course_business_studies",
            title = "Strategic Business Management & Operations",
            category = "Business Studies",
            description = "Explore organizational principles, Fayol & Taylor theories, marketing mix, consumer protection, and modern startup strategy.",
            teacherId = "teacher_ananya",
            teacherName = "Dr. Ananya Iyer",
            teacherCredentials = "Ph.D. Management (IIM-B)",
            priceInr = 1499,
            originalPriceInr = 2499,
            durationHours = 28,
            level = "Beginner",
            ratingAvg = 4.8,
            enrolledCount = 8920,
            nextLiveSession = "Today, 6:30 PM • Porter's 5 Forces Case",
            isLiveNow = false,
            syllabusHighlights = listOf(
                "Principles of Scientific & Administrative Management",
                "Business Environment & PESTEL Analysis",
                "Marketing Mix (4Ps) & Brand Positioning",
                "Financial Planning & Capital Structure Decisions"
            ),
            lessons = listOf(
                LessonItem(
                    id = "bus_l1",
                    title = "Live Case Study: Porter's 5 Forces in Indian Retail",
                    durationMinutes = 50,
                    isLive = true,
                    scheduleLabel = "Today, 6:30 PM",
                    summary = "Analyze competitive rivalry, supplier power, and quick-commerce disruption.",
                    keyTakeaways = listOf(
                        "Evaluate barriers to entry in D2C commerce",
                        "Map substitute threats across omnichannel players"
                    )
                ),
                LessonItem(
                    id = "bus_l2",
                    title = "Delegation vs Decentralization",
                    durationMinutes = 38,
                    isLive = false,
                    scheduleLabel = "Recorded • HD 1080p",
                    summary = "Master authority, responsibility, and accountability chains in scaling enterprises.",
                    keyTakeaways = listOf(
                        "Delegation is compulsory; decentralization is an optional policy choice",
                        "Accountability can never be delegated upward or downward"
                    )
                )
            ),
            studyMaterials = listOf(
                StudyMaterialItem(
                    id = "bus_m1",
                    title = "Fayol's 14 Principles Flash Summary",
                    format = "Revision Card",
                    sizeLabel = "1.1 MB",
                    contentPreview = "1. Division of Work  2. Authority & Responsibility  3. Discipline  4. Unity of Command  5. Unity of Direction  6. Subordination of Individual Interest  7. Remuneration  8. Centralization  9. Scalar Chain  10. Order  11. Equity  12. Stability of Tenure  13. Initiative  14. Esprit de Corps."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    id = "bus_q1",
                    prompt = "Which principle states that an employee should receive orders from only one superior?",
                    options = listOf(
                        "Unity of Direction",
                        "Unity of Command",
                        "Scalar Chain",
                        "Division of Work"
                    ),
                    correctIndex = 1,
                    explanation = "Unity of Command prevents dual subordination and conflicting instructions."
                ),
                QuizQuestion(
                    id = "bus_q2",
                    prompt = "Which element of the Marketing Mix involves warehousing and channel selection?",
                    options = listOf("Product", "Price", "Place", "Promotion"),
                    correctIndex = 2,
                    explanation = "Place (Physical Distribution) covers channels, logistics, inventory, and warehousing."
                )
            )
        ),
        CommerceCourse(
            id = "course_economics_macro",
            title = "Macro & Micro Economics Mastery",
            category = "Economics",
            description = "Deep dive into demand elasticity, market structures, National Income (GDP), Money & Banking, and fiscal policy.",
            teacherId = "teacher_vikram",
            teacherName = "Prof. Vikramaditya Rao",
            teacherCredentials = "M.A. Economics (DSE)",
            priceInr = 1699,
            originalPriceInr = 2999,
            durationHours = 36,
            level = "Intermediate",
            ratingAvg = 4.9,
            enrolledCount = 10340,
            nextLiveSession = "LIVE NOW • RBI Repo Rate & Inflation",
            isLiveNow = true,
            syllabusHighlights = listOf(
                "Consumer Equilibrium & Indifference Curve Analysis",
                "Perfect Competition, Monopoly & Oligopoly",
                "GDP Calculation: Value Added, Income & Expenditure",
                "Central Bank Credit Control & Fiscal Deficit"
            ),
            lessons = listOf(
                LessonItem(
                    id = "eco_l1",
                    title = "Live Session: Monetary Policy Tools & Liquidity",
                    durationMinutes = 60,
                    isLive = true,
                    scheduleLabel = "Live Now • Studio B",
                    summary = "Understand CRR, SLR, Repo Rate, and Open Market Operations with live yield data.",
                    keyTakeaways = listOf(
                        "Higher Repo Rate cools inflation by raising borrowing costs",
                        "OMO purchases inject base money into commercial banks"
                    )
                ),
                LessonItem(
                    id = "eco_l2",
                    title = "National Income Accounting (NDPfc vs GNPmp)",
                    durationMinutes = 45,
                    isLive = false,
                    scheduleLabel = "Recorded • HD 1080p",
                    summary = "Convert Gross to Net, Domestic to National, and Market Price to Factor Cost.",
                    keyTakeaways = listOf(
                        "Gross - Depreciation = Net",
                        "Domestic + NFIA = National",
                        "Market Price - Net Indirect Taxes = Factor Cost"
                    )
                )
            ),
            studyMaterials = listOf(
                StudyMaterialItem(
                    id = "eco_m1",
                    title = "National Income Conversion Formula Matrix",
                    format = "PDF Notes",
                    sizeLabel = "1.4 MB",
                    contentPreview = "NNP at FC (National Income) = GDP at MP - Depreciation + NFIA - Net Indirect Taxes.\nMultiplier (k) = 1 / (1 - MPC) = 1 / MPS."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    id = "eco_q1",
                    prompt = "If Marginal Propensity to Consume (MPC) is 0.75, what is the investment multiplier?",
                    options = listOf("2", "3", "4", "5"),
                    correctIndex = 2,
                    explanation = "Multiplier k = 1 / (1 - MPC) = 1 / (1 - 0.75) = 1 / 0.25 = 4."
                ),
                QuizQuestion(
                    id = "eco_q2",
                    prompt = "To convert Domestic Income into National Income, we add:",
                    options = listOf(
                        "Depreciation",
                        "Net Factor Income from Abroad (NFIA)",
                        "Indirect Taxes",
                        "Subsidies"
                    ),
                    correctIndex = 1,
                    explanation = "National = Domestic + Net Factor Income from Abroad (NFIA)."
                )
            )
        ),
        CommerceCourse(
            id = "course_corporate_finance",
            title = "Corporate Finance & DCF Valuation",
            category = "Finance",
            description = "Learn Time Value of Money, WACC, Capital Budgeting (NPV/IRR), Leverage, and 3-Statement DCF Modeling.",
            teacherId = "teacher_meera",
            teacherName = "CFA Meera Krishnan",
            teacherCredentials = "CFA, FRM",
            priceInr = 2999,
            originalPriceInr = 4999,
            durationHours = 46,
            level = "Advanced",
            ratingAvg = 4.9,
            enrolledCount = 7410,
            nextLiveSession = "Tomorrow, 8:00 PM • WACC & Terminal Value",
            isLiveNow = false,
            syllabusHighlights = listOf(
                "Time Value of Money & Bond Pricing",
                "CAPM, Beta & Weighted Average Cost of Capital",
                "NPV, IRR & Payback Period Capital Budgeting",
                "Free Cash Flow to Firm (FCFF) Valuation"
            ),
            lessons = listOf(
                LessonItem(
                    id = "fin_l1",
                    title = "Live Modeling: Building a 5-Year DCF Model",
                    durationMinutes = 75,
                    isLive = true,
                    scheduleLabel = "Tomorrow, 8:00 PM",
                    summary = "Forecast FCFF, discount at WACC, and sensitize Terminal Growth Rate.",
                    keyTakeaways = listOf(
                        "FCFF = EBIT(1-t) + D&A - CapEx - Change in NWC",
                        "Enterprise Value minus Net Debt equals Equity Value"
                    )
                )
            ),
            studyMaterials = listOf(
                StudyMaterialItem(
                    id = "fin_m1",
                    title = "Corporate Finance Formula Compendium",
                    format = "PDF Handbook",
                    sizeLabel = "2.5 MB",
                    contentPreview = "Cost of Equity (CAPM): Ke = Rf + Beta * (Rm - Rf)\nWACC = (E/V)*Ke + (D/V)*Kd*(1 - TaxRate)\nNPV = Sum[ CFt / (1+r)^t ] - Initial Outlay"
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    id = "fin_q1",
                    prompt = "A capital project should generally be accepted if:",
                    options = listOf(
                        "NPV is negative",
                        "IRR is less than WACC",
                        "NPV is positive and IRR > WACC",
                        "Payback period exceeds asset life"
                    ),
                    correctIndex = 2,
                    explanation = "Positive NPV indicates value creation above the firm's hurdle rate (WACC)."
                )
            )
        ),
        CommerceCourse(
            id = "course_taxation_gst",
            title = "Direct Tax & GST Practical Compliance",
            category = "Taxation",
            description = "Master Income Tax heads, deductions under Chapter VI-A, TDS compliance, and GST Input Tax Credit & GSTR filings.",
            teacherId = "teacher_raghav",
            teacherName = "CA Raghav Mehta",
            teacherCredentials = "FCA, DISA (ICAI)",
            priceInr = 2499,
            originalPriceInr = 3999,
            durationHours = 38,
            level = "Intermediate",
            ratingAvg = 4.9,
            enrolledCount = 9180,
            nextLiveSession = "Friday, 7:00 PM • GST ITC Rule 36(4)",
            isLiveNow = false,
            syllabusHighlights = listOf(
                "Residential Status & Five Heads of Income",
                "PGBP Depreciation & Capital Gains Indexation",
                "TDS Sections (192, 194C, 194J) & Advance Tax",
                "CGST, SGST, IGST & Input Tax Credit Ledger"
            ),
            lessons = listOf(
                LessonItem(
                    id = "tax_l1",
                    title = "Live Clinic: GST Input Tax Credit Set-off Rules",
                    durationMinutes = 55,
                    isLive = true,
                    scheduleLabel = "Friday, 7:00 PM",
                    summary = "Step-by-step utilization order for IGST, CGST, and SGST electronic credit ledgers.",
                    keyTakeaways = listOf(
                        "IGST credit must be exhausted completely before using CGST or SGST",
                        "CGST credit cannot be utilized to pay SGST liability"
                    )
                )
            ),
            studyMaterials = listOf(
                StudyMaterialItem(
                    id = "tax_m1",
                    title = "TDS Rates & Deductions Quick Reference",
                    format = "Tax Table",
                    sizeLabel = "1.6 MB",
                    contentPreview = "Sec 194C: Contractors (1% Ind/HUF, 2% Others)\nSec 194J: Professional Fees (10%)\nSec 194I: Rent (2% Plant/Machinery, 10% Land/Building)\nSec 80C Max Limit: Rs. 1,50,000"
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    id = "tax_q1",
                    prompt = "Which GST credit must be exhausted first before utilizing CGST or SGST credits?",
                    options = listOf("CGST", "SGST", "IGST", "Cess"),
                    correctIndex = 2,
                    explanation = "Under Section 49(5) of CGST Act, IGST input credit is utilized first against IGST, then CGST/SGST."
                )
            )
        ),
        CommerceCourse(
            id = "course_ca_foundation",
            title = "CA Foundation Complete Bootcamp (Papers 1–4)",
            category = "CA Foundation",
            description = "Structured exam preparation covering Accounting, Business Laws, Quantitative Aptitude, and Business Economics with mock tests.",
            teacherId = "teacher_raghav",
            teacherName = "CA Raghav Mehta",
            teacherCredentials = "FCA, DISA (ICAI)",
            priceInr = 4999,
            originalPriceInr = 7999,
            durationHours = 120,
            level = "Foundation",
            ratingAvg = 5.0,
            enrolledCount = 15800,
            nextLiveSession = "Daily 5:00 PM • ICAI RTP Marathon",
            isLiveNow = false,
            syllabusHighlights = listOf(
                "Paper 1: Accounting & Partnership / Company Accounts",
                "Paper 2: Indian Contract Act, Sale of Goods & LLP",
                "Paper 3: Time Value of Money, Permutations & Stats",
                "Paper 4: Business Economics & Monetary Markets"
            ),
            lessons = listOf(
                LessonItem(
                    id = "ca_l1",
                    title = "Indian Contract Act 1872: Offer, Acceptance & Consideration",
                    durationMinutes = 62,
                    isLive = false,
                    scheduleLabel = "Recorded • HD 1080p",
                    summary = "High-yield ICAI case law breakdowns for Paper 2 subjective questions.",
                    keyTakeaways = listOf(
                        "Carlill v. Carbolic Smoke Ball Co. — General Offer",
                        "Stranger to contract vs Stranger to consideration"
                    )
                )
            ),
            studyMaterials = listOf(
                StudyMaterialItem(
                    id = "ca_m1",
                    title = "CA Foundation Landmark Case Laws Digest",
                    format = "PDF Digest",
                    sizeLabel = "2.9 MB",
                    contentPreview = "Covers Balfour v. Balfour, Mohori Bibee v. Dharmodas Ghose, Hadley v. Baxendale, and Salomon v. Salomon & Co."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    id = "ca_q1",
                    prompt = "An agreement with a minor is:",
                    options = listOf(
                        "Voidable at the minor's option",
                        "Void ab initio",
                        "Valid if ratified at age 18",
                        "Illegal"
                    ),
                    correctIndex = 1,
                    explanation = "Per Mohori Bibee v. Dharmodas Ghose, a minor's agreement is void ab initio (void from the beginning)."
                )
            )
        ),
        CommerceCourse(
            id = "course_cs_foundation",
            title = "CS Foundation & Corporate Law Essentials",
            category = "CS Foundation",
            description = "Build mastery in Company Law, Secretarial Practice, Business Communication, and Legal Aptitude for ICSI examinations.",
            teacherId = "teacher_siddharth",
            teacherName = "CS Siddharth Kapoor",
            teacherCredentials = "FCS, LL.B.",
            priceInr = 3499,
            originalPriceInr = 5499,
            durationHours = 72,
            level = "Foundation",
            ratingAvg = 4.8,
            enrolledCount = 6350,
            nextLiveSession = "Saturday, 11:00 AM • Board Resolutions",
            isLiveNow = false,
            syllabusHighlights = listOf(
                "Incorporation of Companies & MOA/AOA",
                "Board Meetings, Quorum & Secretarial Standards",
                "Share Capital, Debentures & Dividend Rules",
                "Business Communication & Legal Drafting"
            ),
            lessons = listOf(
                LessonItem(
                    id = "cs_l1",
                    title = "MOA vs AOA & Doctrine of Ultra Vires",
                    durationMinutes = 48,
                    isLive = false,
                    scheduleLabel = "Recorded • HD 1080p",
                    summary = "Understand charter documents, constructive notice, and indoor management.",
                    keyTakeaways = listOf(
                        "Royal British Bank v. Turquand — Doctrine of Indoor Management",
                        "Ultra vires acts cannot be ratified even by unanimous shareholders"
                    )
                )
            ),
            studyMaterials = listOf(
                StudyMaterialItem(
                    id = "cs_m1",
                    title = "Companies Act 2013 Section Navigator",
                    format = "PDF Reference",
                    sizeLabel = "2.1 MB",
                    contentPreview = "Sec 2(68) Private Company, Sec 2(71) Public Company, Sec 149 Board Composition, Sec 173 Board Meetings (min 4/year)."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    id = "cs_q1",
                    prompt = "What is the minimum number of Board Meetings a company must hold in a calendar year under Sec 173?",
                    options = listOf("2", "3", "4", "6"),
                    correctIndex = 2,
                    explanation = "Every company must hold at least 4 Board Meetings every year with max 120 days gap between two meetings."
                )
            )
        ),
        CommerceCourse(
            id = "course_stock_market",
            title = "Stock Market Basics & Equity Investing",
            category = "Stock Market Basics",
            description = "Learn how stock exchanges work, fundamental P/E & ROE screening, candlestick basics, IPO analysis, and risk management.",
            teacherId = "teacher_meera",
            teacherName = "CFA Meera Krishnan",
            teacherCredentials = "CFA, FRM",
            priceInr = 1299,
            originalPriceInr = 2499,
            durationHours = 22,
            level = "Beginner",
            ratingAvg = 4.9,
            enrolledCount = 19400,
            nextLiveSession = "LIVE NOW • Reading Annual Reports",
            isLiveNow = true,
            syllabusHighlights = listOf(
                "Primary vs Secondary Markets, NSE/BSE & SEBI",
                "Fundamental Ratios: P/E, P/B, ROCE & Debt-Equity",
                "Support, Resistance & Moving Averages",
                "Position Sizing & Long-Term Compounding"
            ),
            lessons = listOf(
                LessonItem(
                    id = "stk_l1",
                    title = "Live Market Lab: Screening Quality Compounders",
                    durationMinutes = 52,
                    isLive = true,
                    scheduleLabel = "Live Now • Market Lab",
                    summary = "Evaluate ROCE > 20%, free cash flow conversion, and promoter pledging.",
                    keyTakeaways = listOf(
                        "Consistent high ROCE signals durable competitive moat",
                        "Always verify CFO/PAT ratio >= 0.8 over 5 years"
                    )
                )
            ),
            studyMaterials = listOf(
                StudyMaterialItem(
                    id = "stk_m1",
                    title = "10-Point Equity Fundamental Checklist",
                    format = "Checklist PDF",
                    sizeLabel = "1.2 MB",
                    contentPreview = "1. Revenue CAGR > 12%  2. Operating Margin stability  3. ROCE > 18%  4. Debt/Equity < 0.5  5. Positive Free Cash Flow  6. Reasonable PEG < 1.5"
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    id = "stk_q1",
                    prompt = "Which metric best measures how efficiently a company generates profits from all capital employed?",
                    options = listOf("Dividend Yield", "ROCE", "Beta", "Face Value"),
                    correctIndex = 1,
                    explanation = "Return on Capital Employed (ROCE = EBIT / Capital Employed) measures overall capital efficiency."
                )
            )
        )
    )
}
