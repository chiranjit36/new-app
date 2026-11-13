package com.example.data

import com.example.R
import com.example.model.CommerceCourse
import com.example.model.LessonItem
import com.example.model.QuizQuestion
import com.example.model.StudyMaterialItem
import com.example.model.SubscriptionPlanOption
import com.example.model.TeacherProfile

object CatalogData {

    const val CENTER_NAME = "COMMERCETIME"
    const val CENTER_SUBTITLE = "Apurba Da's Classes • A Unique Coaching Center for Commerce Students"
    const val CENTER_ADDRESS = "Zilla School More, Near Rohan Fast Food, Purulia (W.B.)"
    const val CENTER_PHONE = "8653767405"
    const val CENTER_SOCIAL = "Facebook / YouTube : COMMERCETIME"

    val categories = listOf(
        "All",
        "Class 11 & 12",
        "B.Com",
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
            id = "teacher_apurba",
            name = "Apurba Sarkar",
            credentials = "M.COM, MBA • Lead Mentor",
            specialty = "Class XI, Class XII, B.Com, BBA, MBA & CA Foundation",
            experienceYears = 14,
            rating = 5.0,
            studentsTaught = "15,000+",
            bio = "Founder of COMMERCETIME (Apurba Da's Classes) in Purulia, W.B. Teaching Accountancy, Costing, Taxation, and Finance in Bengali, English & Hindi medium for over 14 years with Smart AI Digital Board & AC Classroom.",
            photoResId = R.drawable.img_apurba_podium
        ),
        TeacherProfile(
            id = "teacher_ranjit",
            name = "Ranjit Kr. Sahis",
            credentials = "M.A., M.Phil.",
            specialty = "Economics & Business Communication",
            experienceYears = 11,
            rating = 4.9,
            studentsTaught = "8,400+",
            bio = "Senior faculty at COMMERCETIME guiding Class 11, 12 & B.Com Honours students in Microeconomics, Macroeconomics, and Indian Economy."
        ),
        TeacherProfile(
            id = "teacher_nayan",
            name = "Nayan Kr. Majhi",
            credentials = "M.Sc., B.Ed.",
            specialty = "Business Mathematics & Statistics",
            experienceYears = 10,
            rating = 4.9,
            studentsTaught = "7,900+",
            bio = "Quantitative Aptitude & Business Statistics specialist simplifying probability, matrices, and financial mathematics for B.Com and CA Foundation."
        ),
        TeacherProfile(
            id = "teacher_atanu",
            name = "Atanu Majhi",
            credentials = "M.Sc., B.Ed.",
            specialty = "Quantitative Methods & Reasoning",
            experienceYears = 9,
            rating = 4.8,
            studentsTaught = "6,500+",
            bio = "Dedicated faculty at COMMERCETIME helping Class XI, XII and B.Com students achieve top marks in numerical papers."
        )
    )

    // The 4 flagship affordable batches requested for COMMERCETIME
    val flagshipBatchIds = setOf(
        "batch_class_11_12",
        "batch_bcom_1st_year",
        "batch_bcom_1_2_year",
        "batch_bcom_3_year"
    )

    val subscriptionPlans = listOf(
        SubscriptionPlanOption(
            id = "free",
            name = "Starter Access",
            priceInr = 0,
            billingCycle = "Free",
            badge = "BASIC",
            perks = listOf(
                "Preview classes & demo lectures by Apurba Sarkar",
                "Daily attendance & streak tracker",
                "24x7 E-Learning AI Assistant"
            )
        ),
        SubscriptionPlanOption(
            id = "pro_monthly",
            name = "B.Com 1st Year Pass",
            priceInr = 149,
            billingCycle = "Both Semesters",
            badge = "POPULAR",
            perks = listOf(
                "Full access to B.Com Sem 1 & Sem 2 classes",
                "Recorded Smart Digital Board lectures + PDF notes",
                "Chapter quizzes & completion certificate"
            )
        ),
        SubscriptionPlanOption(
            id = "all_access_annual",
            name = "B.Com 3 Years Complete",
            priceInr = 249,
            billingCycle = "Full 3-Year Access",
            badge = "BEST VALUE",
            perks = listOf(
                "All 6 Semesters of B.Com (1st, 2nd & 3rd Year)",
                "Bengali, English & Hindi medium recorded lectures",
                "24x7 Gemini AI Doubt Solver + Exam Mock Tests",
                "Priority offline/online doubt support"
            )
        )
    )

    val curatedCourses: List<CommerceCourse> = listOf(
        CommerceCourse(
            id = "batch_class_11_12",
            title = "Class 11 & 12 Both — Full Commerce Batch",
            category = "Class 11 & 12",
            description = "Complete Accountancy, Cost & Tax, Business Studies, and Economics for both Class XI & Class XII (WBCHSE / CBSE / ISC) by Apurba Sarkar.",
            teacherId = "teacher_apurba",
            teacherName = "Apurba Sarkar",
            teacherCredentials = "M.COM, MBA • 14+ Yrs Exp",
            priceInr = 99,
            originalPriceInr = 999,
            durationHours = 110,
            level = "Foundation",
            ratingAvg = 5.0,
            enrolledCount = 4820,
            nextLiveSession = "LIVE NOW • Class 11 & 12 Accountancy Digital Board",
            isLiveNow = true,
            syllabusHighlights = listOf(
                "Class 11: Journal, Ledger, Trial Balance, BRS & Final Accounts",
                "Class 12: Partnership, Admission/Retirement, Company Shares & Cash Flow",
                "Business Studies & Economics in Bengali, English & Hindi medium",
                "Smart AI Digital Board Recorded Lectures + Solved PDF Notes"
            ),
            lessons = listOf(
                LessonItem(
                    id = "c1112_l1",
                    title = "Live Class: Partnership Accounts & Profit Appropriation (Class 12)",
                    durationMinutes = 60,
                    isLive = true,
                    scheduleLabel = "Live Now • Smart Digital Board",
                    summary = "Apurba Sarkar solves Profit & Loss Appropriation Account and Capital Accounts step by step.",
                    keyTakeaways = listOf(
                        "Interest on Capital vs Interest on Drawings rules",
                        "Fixed vs Fluctuating Capital Account format"
                    )
                ),
                LessonItem(
                    id = "c1112_l2",
                    title = "Recorded: Golden Rules of Accounting & Journal Entries (Class 11)",
                    durationMinutes = 50,
                    isLive = false,
                    scheduleLabel = "Recorded • Bengali / English / Hindi",
                    summary = "Crystal-clear foundation of Personal, Real, and Nominal accounts with 25 solved transactions.",
                    keyTakeaways = listOf(
                        "Debit what comes in, Credit what goes out",
                        "Accrued & Prepaid adjustments in Final Accounts"
                    )
                )
            ),
            studyMaterials = listOf(
                StudyMaterialItem(
                    id = "c1112_m1",
                    title = "Class 11 & 12 Final Accounts Adjustment Master Sheet",
                    format = "PDF Notes",
                    sizeLabel = "1.6 MB",
                    contentPreview = "1. Closing Stock: Trading A/c (Cr) & Balance Sheet (Assets)\n2. Outstanding Expense: Add to Expense & Balance Sheet (Liabilities)\n3. Prepaid Expense: Deduct from Expense & Balance Sheet (Assets)\n4. Depreciation: P&L (Dr) & Deduct from Fixed Asset"
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    id = "c1112_q1",
                    prompt = "Goodwill is classified under which type of account?",
                    options = listOf("Nominal Account", "Personal Account", "Real Account (Intangible Asset)", "Liability Account"),
                    correctIndex = 2,
                    explanation = "Goodwill is an intangible asset, therefore it is a Real Account."
                ),
                QuizQuestion(
                    id = "c1112_q2",
                    prompt = "In absence of a Partnership Deed, interest on partner's loan is allowed at:",
                    options = listOf("12% p.a.", "6% p.a.", "9% p.a.", "No interest"),
                    correctIndex = 1,
                    explanation = "Under the Indian Partnership Act 1932, interest on loan by a partner is 6% p.a."
                )
            )
        ),
        CommerceCourse(
            id = "batch_bcom_1st_year",
            title = "B.Com 1st Year — Both Semesters (Sem 1 & 2)",
            category = "B.Com",
            description = "Full access to B.Com 1st Year Semester 1 & Semester 2: Financial Accounting, Business Law, Cost Accounting Basics & Economics.",
            teacherId = "teacher_apurba",
            teacherName = "Apurba Sarkar",
            teacherCredentials = "M.COM, MBA • 14+ Yrs Exp",
            priceInr = 149,
            originalPriceInr = 1499,
            durationHours = 95,
            level = "Foundation",
            ratingAvg = 5.0,
            enrolledCount = 3940,
            nextLiveSession = "Today, 6:00 PM • B.Com Sem 1 Financial Accounting",
            isLiveNow = true,
            syllabusHighlights = listOf(
                "Sem 1 & Sem 2 Complete University Syllabus Coverage",
                "Consignment, Joint Venture, Branch & Departmental Accounts",
                "Business Regulatory Framework & Partnership Law",
                "Previous 10 Years University Paper Solutions"
            ),
            lessons = listOf(
                LessonItem(
                    id = "bc1_l1",
                    title = "Live Class: Branch & Departmental Accounting (Sem 1)",
                    durationMinutes = 65,
                    isLive = true,
                    scheduleLabel = "Today, 6:00 PM • AC Digital Studio",
                    summary = "Debtors method and Stock & Debtors method solved cleanly for B.Com Honours & General.",
                    keyTakeaways = listOf(
                        "Invoice price loading adjustment in Branch Stock Account",
                        "Inter-departmental transfer unrealized profit reserve"
                    )
                ),
                LessonItem(
                    id = "bc1_l2",
                    title = "Recorded: Consignment Accounts & Abnormal Loss (Sem 2)",
                    durationMinutes = 52,
                    isLive = false,
                    scheduleLabel = "Recorded • HD Digital Board",
                    summary = "Calculate unsold stock valuation and abnormal loss claim with insurance adjustment.",
                    keyTakeaways = listOf(
                        "Include consignor + non-recurring consignee expenses in stock",
                        "Normal loss inflates per-unit cost of remaining goods"
                    )
                )
            ),
            studyMaterials = listOf(
                StudyMaterialItem(
                    id = "bc1_m1",
                    title = "B.Com 1st Year Sem 1 & 2 Important Suggestions & Formulas",
                    format = "PDF Digest",
                    sizeLabel = "2.2 MB",
                    contentPreview = "Covers Consignment Valuation, Hire Purchase Interest Calculation, Departmental Allocation Ratios, and Indian Contract Act essentials."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    id = "bc1_q1",
                    prompt = "In Consignment Accounts, Del Credere commission is paid to the consignee for:",
                    options = listOf("Cash sales only", "Bearing bad debts risk on credit sales", "Storing goods safely", "Paying freight charges"),
                    correctIndex = 1,
                    explanation = "Del Credere commission compensates the consignee for guaranteeing collection from credit customers."
                )
            )
        ),
        CommerceCourse(
            id = "batch_bcom_1_2_year",
            title = "B.Com 1st & 2nd Year — Both Years Access (Sem 1–4)",
            category = "B.Com",
            description = "Combined 2-Year B.Com pack covering Semesters 1, 2, 3 & 4: Financial, Cost, Corporate Accounting, Income Tax & Business Stats.",
            teacherId = "teacher_apurba",
            teacherName = "Apurba Sarkar",
            teacherCredentials = "M.COM, MBA • 14+ Yrs Exp",
            priceInr = 199,
            originalPriceInr = 2499,
            durationHours = 180,
            level = "Intermediate",
            ratingAvg = 4.9,
            enrolledCount = 3150,
            nextLiveSession = "Tomorrow, 7:30 PM • Cost Sheet & Process Costing",
            isLiveNow = false,
            syllabusHighlights = listOf(
                "Sem 1 & 2: Financial Accounting, Consignment, Branch & Law",
                "Sem 3 & 4: Cost Accounting, Corporate Accounting & Direct Tax",
                "Issue/Forfeiture of Shares, Redemption of Debentures & Goodwill",
                "Joint Faculty: Apurba Sarkar & Nayan Kr. Majhi (Stats)"
            ),
            lessons = listOf(
                LessonItem(
                    id = "bc12_l1",
                    title = "Live Workshop: Cost Sheet, Prime Cost & Factory Overheads",
                    durationMinutes = 60,
                    isLive = true,
                    scheduleLabel = "Tomorrow, 7:30 PM",
                    summary = "Prepare complete Cost Sheets and reconcile Cost vs Financial Profits.",
                    keyTakeaways = listOf(
                        "Direct Material + Direct Labour + Direct Expenses = Prime Cost",
                        "Treatment of Opening & Closing WIP and Finished Goods"
                    )
                )
            ),
            studyMaterials = listOf(
                StudyMaterialItem(
                    id = "bc12_m1",
                    title = "Cost & Corporate Accounting Workbook (Sem 1–4)",
                    format = "PDF Workbook",
                    sizeLabel = "3.4 MB",
                    contentPreview = "Includes EOQ formula, FIFO/LIFO stores ledger, Process Costing abnormal gain/loss, and Amalgamation journal entries."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    id = "bc12_q1",
                    prompt = "Prime Cost plus Works/Factory Overheads equals:",
                    options = listOf("Cost of Production", "Factory / Works Cost", "Cost of Sales", "Gross Margin"),
                    correctIndex = 1,
                    explanation = "Works Cost = Prime Cost + Factory Overheads (adjusted for WIP)."
                )
            )
        ),
        CommerceCourse(
            id = "batch_bcom_3_year",
            title = "B.Com All 3 Years — Full Complete Access (Sem 1–6)",
            category = "B.Com",
            description = "Everything in B.Com 1st, 2nd & 3rd Year (All 6 Semesters): Financial, Cost, Corporate, Auditing, GST, Financial Management & Project Prep.",
            teacherId = "teacher_apurba",
            teacherName = "Apurba Sarkar",
            teacherCredentials = "M.COM, MBA • 14+ Yrs Exp",
            priceInr = 249,
            originalPriceInr = 3999,
            durationHours = 280,
            level = "Advanced",
            ratingAvg = 5.0,
            enrolledCount = 5410,
            nextLiveSession = "LIVE NOW • B.Com Sem 5/6 Financial Management & GST",
            isLiveNow = true,
            syllabusHighlights = listOf(
                "Complete 3-Year B.Com (Semesters 1 to 6) Unlimited Access",
                "Financial Management, Capital Budgeting, WACC & Ratio Analysis",
                "Direct Tax, GST Compliance, Auditing & Corporate Governance",
                "Bengali, English & Hindi Medium + 24x7 AI Study Support"
            ),
            lessons = listOf(
                LessonItem(
                    id = "bc3_l1",
                    title = "Live Masterclass: Financial Management & Capital Budgeting",
                    durationMinutes = 70,
                    isLive = true,
                    scheduleLabel = "Live Now • COMMERCETIME Hall",
                    summary = "NPV, IRR, Payback Period & Leverages solved for B.Com 3rd Year Honours.",
                    keyTakeaways = listOf(
                        "Operating Leverage = Contribution / EBIT",
                        "Combined Leverage = Operating Leverage x Financial Leverage"
                    )
                ),
                LessonItem(
                    id = "bc3_l2",
                    title = "Recorded: Auditing & GST Input Tax Credit (Sem 5 & 6)",
                    durationMinutes = 58,
                    isLive = false,
                    scheduleLabel = "Recorded • HD 1080p",
                    summary = "Vouching, verification, auditor duties, and practical GST tax invoice rules.",
                    keyTakeaways = listOf(
                        "IGST credit utilization sequence under Sec 49(5)",
                        "Internal Check vs Internal Audit distinctions"
                    )
                )
            ),
            studyMaterials = listOf(
                StudyMaterialItem(
                    id = "bc3_m1",
                    title = "B.Com 3-Year Master Formula & Tax Compendium",
                    format = "Complete Guide",
                    sizeLabel = "4.1 MB",
                    contentPreview = "All ratios, Leverages, EOQ, Standard Costing variances, Income Tax slab rates, and GST ITC rules in one handbook."
                )
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    id = "bc3_q1",
                    prompt = "Financial Leverage measures the sensitivity of EPS to changes in:",
                    options = listOf("Sales Revenue", "EBIT (Operating Profit)", "Fixed Factory Costs", "Working Capital"),
                    correctIndex = 1,
                    explanation = "Degree of Financial Leverage = % change in EPS / % change in EBIT."
                )
            )
        ),
        CommerceCourse(
            id = "course_accounting_mastery",
            title = "Financial Accounting & IFRS Mastery",
            category = "Accounting",
            description = "Double-entry bookkeeping, trial balances, depreciation, bank reconciliation, and Ind AS / IFRS statements.",
            teacherId = "teacher_apurba",
            teacherName = "Apurba Sarkar",
            teacherCredentials = "M.COM, MBA",
            priceInr = 99,
            originalPriceInr = 799,
            durationHours = 42,
            level = "Foundation",
            ratingAvg = 4.9,
            enrolledCount = 2480,
            nextLiveSession = "Sat, 5:00 PM • BRS & Depreciation",
            isLiveNow = false,
            syllabusHighlights = listOf("Journal & Ledger Mastery", "Cash Flow & Final Accounts"),
            lessons = listOf(
                LessonItem("acc_l1", "Trial Balance & Error Rectification", 48, false, "Recorded • HD", "Detect and rectify one-sided and two-sided accounting errors.", listOf("Suspense Account balancing"))
            ),
            studyMaterials = listOf(
                StudyMaterialItem("acc_m1", "Accounting Ratios Cheat Sheet", "PDF Guide", "1.4 MB", "Current Ratio = CA / CL; Quick Ratio = (CA - Inventory) / CL; Debt-Equity = Total Debt / Equity.")
            ),
            quizQuestions = listOf(
                QuizQuestion("acc_q1", "A credit balance in the Bank Pass Book indicates:", listOf("Overdraft", "Favorable bank balance", "Dishonored cheque", "Bank charges"), 1, "Customer deposits are liabilities for the bank, showing a credit balance.")
            )
        ),
        CommerceCourse(
            id = "course_business_studies",
            title = "Business Studies & Management Principles",
            category = "Business Studies",
            description = "Fayol & Taylor principles, Marketing Mix, Business Environment, and BBA/MBA foundational strategy.",
            teacherId = "teacher_ranjit",
            teacherName = "Ranjit Kr. Sahis",
            teacherCredentials = "M.A., M.Phil.",
            priceInr = 99,
            originalPriceInr = 699,
            durationHours = 28,
            level = "Beginner",
            ratingAvg = 4.8,
            enrolledCount = 1920,
            nextLiveSession = "Mon, 6:30 PM • Marketing Mix",
            isLiveNow = false,
            syllabusHighlights = listOf("Management Principles", "Marketing & Consumer Protection"),
            lessons = listOf(
                LessonItem("bus_l1", "Fayol's 14 Principles of Management", 40, false, "Recorded • HD", "Real-world examples for Class 12 & BBA exams.", listOf("Unity of Command vs Unity of Direction"))
            ),
            studyMaterials = listOf(
                StudyMaterialItem("bus_m1", "Fayol's 14 Principles Card", "Revision PDF", "1.1 MB", "Division of Work, Authority, Discipline, Unity of Command, Unity of Direction, Scalar Chain, Esprit de Corps.")
            ),
            quizQuestions = listOf(
                QuizQuestion("bus_q1", "Receiving orders from only one superior is:", listOf("Unity of Direction", "Unity of Command", "Scalar Chain", "Equity"), 1, "Unity of Command prevents dual subordination.")
            )
        ),
        CommerceCourse(
            id = "course_economics_macro",
            title = "Micro & Macro Economics for Class 11–12 & B.Com",
            category = "Economics",
            description = "Demand elasticity, market structures, National Income (GDP), Money & Banking, and fiscal policy.",
            teacherId = "teacher_ranjit",
            teacherName = "Ranjit Kr. Sahis",
            teacherCredentials = "M.A., M.Phil.",
            priceInr = 149,
            originalPriceInr = 899,
            durationHours = 36,
            level = "Intermediate",
            ratingAvg = 4.9,
            enrolledCount = 2340,
            nextLiveSession = "Wed, 7:00 PM • National Income",
            isLiveNow = false,
            syllabusHighlights = listOf("GDP & National Income", "Monetary & Fiscal Policy"),
            lessons = listOf(
                LessonItem("eco_l1", "National Income Accounting (GDP to NNPfc)", 45, false, "Recorded • HD", "Value added, Income, and Expenditure methods.", listOf("Domestic + NFIA = National"))
            ),
            studyMaterials = listOf(
                StudyMaterialItem("eco_m1", "National Income Conversion Matrix", "PDF Notes", "1.3 MB", "NNP at FC = GDP at MP - Depreciation + NFIA - Net Indirect Taxes.")
            ),
            quizQuestions = listOf(
                QuizQuestion("eco_q1", "If MPC = 0.75, the investment multiplier is:", listOf("2", "3", "4", "5"), 2, "k = 1 / (1 - 0.75) = 4.")
            )
        ),
        CommerceCourse(
            id = "course_taxation_gst",
            title = "Income Tax & GST Practical Compliance",
            category = "Taxation",
            description = "Heads of Income, Chapter VI-A deductions, TDS, and GST Input Tax Credit for B.Com & CA.",
            teacherId = "teacher_apurba",
            teacherName = "Apurba Sarkar",
            teacherCredentials = "M.COM, MBA",
            priceInr = 199,
            originalPriceInr = 1299,
            durationHours = 38,
            level = "Intermediate",
            ratingAvg = 4.9,
            enrolledCount = 2180,
            nextLiveSession = "Fri, 7:00 PM • GST Set-off Rules",
            isLiveNow = false,
            syllabusHighlights = listOf("Five Heads of Income", "CGST/SGST/IGST ITC Ledger"),
            lessons = listOf(
                LessonItem("tax_l1", "GST Input Tax Credit Set-off Sequence", 55, false, "Recorded • HD", "Section 49(5) credit utilization rules.", listOf("IGST exhausted first"))
            ),
            studyMaterials = listOf(
                StudyMaterialItem("tax_m1", "TDS & Sec 80C Reference", "Tax Table", "1.5 MB", "Sec 194C (1%/2%), Sec 194J (10%), Sec 80C Max Rs 1,50,000.")
            ),
            quizQuestions = listOf(
                QuizQuestion("tax_q1", "Which GST credit must be exhausted first?", listOf("CGST", "SGST", "IGST", "Cess"), 2, "IGST credit must be exhausted completely before CGST or SGST.")
            )
        ),
        CommerceCourse(
            id = "course_ca_foundation",
            title = "CA Foundation Complete Prep (Papers 1–4)",
            category = "CA Foundation",
            description = "Accounting, Business Laws, Quantitative Aptitude (Nayan & Atanu Sir), and Business Economics by COMMERCETIME.",
            teacherId = "teacher_apurba",
            teacherName = "Apurba Sarkar & Team",
            teacherCredentials = "COMMERCETIME Faculty",
            priceInr = 249,
            originalPriceInr = 2999,
            durationHours = 120,
            level = "Foundation",
            ratingAvg = 5.0,
            enrolledCount = 2800,
            nextLiveSession = "Sun, 10:00 AM • ICAI RTP Marathon",
            isLiveNow = false,
            syllabusHighlights = listOf("CA Paper 1–4 Full Coverage", "Quantitative Aptitude & Law"),
            lessons = listOf(
                LessonItem("ca_l1", "Indian Contract Act 1872 Essentials", 62, false, "Recorded • HD", "Offer, acceptance, consideration & minor agreements.", listOf("Void ab initio agreements"))
            ),
            studyMaterials = listOf(
                StudyMaterialItem("ca_m1", "Landmark Case Laws Digest", "PDF Digest", "2.4 MB", "Carlill v. Carbolic Smoke Ball Co., Mohori Bibee v. Dharmodas Ghose.")
            ),
            quizQuestions = listOf(
                QuizQuestion("ca_q1", "An agreement with a minor is:", listOf("Voidable", "Void ab initio", "Valid", "Illegal"), 1, "Mohori Bibee v. Dharmodas Ghose established minor agreements are void ab initio.")
            )
        ),
        CommerceCourse(
            id = "course_corporate_finance",
            title = "Corporate Finance, BBA/MBA & Stock Market Basics",
            category = "Finance",
            description = "Time Value of Money, Capital Budgeting (NPV/IRR), P/E & ROE stock screening, and financial modeling.",
            teacherId = "teacher_nayan",
            teacherName = "Nayan Kr. Majhi & Apurba Sarkar",
            teacherCredentials = "COMMERCETIME Faculty",
            priceInr = 199,
            originalPriceInr = 1499,
            durationHours = 40,
            level = "Intermediate",
            ratingAvg = 4.9,
            enrolledCount = 1950,
            nextLiveSession = "Sat, 8:00 PM • Equity Valuation",
            isLiveNow = false,
            syllabusHighlights = listOf("NPV, IRR & WACC", "Stock Exchange & Fundamental Ratios"),
            lessons = listOf(
                LessonItem("fin_l1", "Time Value of Money & NPV Analysis", 50, false, "Recorded • HD", "Discounting future cash flows for project decisions.", listOf("Accept if NPV > 0"))
            ),
            studyMaterials = listOf(
                StudyMaterialItem("fin_m1", "Finance & Stock Ratio Checklist", "PDF Guide", "1.5 MB", "ROE = Net Profit / Equity; ROCE = EBIT / Capital Employed; P/E = Price / EPS.")
            ),
            quizQuestions = listOf(
                QuizQuestion("fin_q1", "A capital project is financially viable when:", listOf("NPV < 0", "IRR < WACC", "NPV > 0 and IRR > WACC", "Debt is zero"), 2, "Positive NPV creates shareholder value above WACC.")
            )
        ),
        CommerceCourse(
            id = "course_cs_foundation",
            title = "CS Foundation & Corporate Law",
            category = "CS Foundation",
            description = "Companies Act 2013, MOA/AOA, Board Meetings, and Secretarial Practice.",
            teacherId = "teacher_atanu",
            teacherName = "Atanu Majhi & Apurba Sarkar",
            teacherCredentials = "COMMERCETIME Faculty",
            priceInr = 199,
            originalPriceInr = 1499,
            durationHours = 60,
            level = "Foundation",
            ratingAvg = 4.8,
            enrolledCount = 1120,
            nextLiveSession = "Thu, 6:00 PM • Companies Act Sec 173",
            isLiveNow = false,
            syllabusHighlights = listOf("MOA, AOA & Ultra Vires", "Board Meetings & Quorum"),
            lessons = listOf(
                LessonItem("cs_l1", "MOA vs AOA & Indoor Management", 46, false, "Recorded • HD", "Doctrine of Constructive Notice & Turquand Rule.", listOf("4 Board Meetings/year"))
            ),
            studyMaterials = listOf(
                StudyMaterialItem("cs_m1", "Companies Act Quick Navigator", "PDF Notes", "1.8 MB", "Sec 2(68) Private Co, Sec 2(71) Public Co, Sec 173 Board Meetings.")
            ),
            quizQuestions = listOf(
                QuizQuestion("cs_q1", "Minimum Board Meetings per calendar year under Sec 173:", listOf("2", "3", "4", "6"), 2, "At least 4 Board Meetings every year.")
            )
        ),
        CommerceCourse(
            id = "course_stock_market",
            title = "Stock Market Basics & Portfolio Investing",
            category = "Stock Market Basics",
            description = "NSE/BSE mechanics, fundamental P/E & ROCE screening, IPO analysis, and risk management.",
            teacherId = "teacher_apurba",
            teacherName = "Apurba Sarkar",
            teacherCredentials = "M.COM, MBA",
            priceInr = 99,
            originalPriceInr = 999,
            durationHours = 22,
            level = "Beginner",
            ratingAvg = 4.9,
            enrolledCount = 3100,
            nextLiveSession = "Sun, 4:00 PM • Live Annual Report Lab",
            isLiveNow = false,
            syllabusHighlights = listOf("NSE/BSE & SEBI Basics", "P/E, ROCE & Cash Flow Screening"),
            lessons = listOf(
                LessonItem("stk_l1", "Screening Quality Compounding Stocks", 45, false, "Recorded • HD", "Check ROCE > 18% and low debt-to-equity.", listOf("Verify CFO/PAT conversion"))
            ),
            studyMaterials = listOf(
                StudyMaterialItem("stk_m1", "10-Point Stock Checklist", "PDF Checklist", "1.1 MB", "Revenue growth, ROCE, Operating Margin, Debt/Equity, Free Cash Flow.")
            ),
            quizQuestions = listOf(
                QuizQuestion("stk_q1", "Which ratio measures return on total capital employed?", listOf("Dividend Yield", "ROCE", "Beta", "Face Value"), 1, "ROCE = EBIT / Capital Employed.")
            )
        )
    )
}
