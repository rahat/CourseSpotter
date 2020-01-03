/****** Object:  Table [dbo].[CourseSpotter]    Script Date: 12/8/2019 6:14:29 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[CourseSpotter](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Email] [varchar](30) NULL,
	[Phone] [varchar](15) NULL,
	[College] [varchar](30) NOT NULL,
	[CourseName] [varchar](30) NOT NULL,
	[CourseNumber] [int] NOT NULL,
	[ClassID] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO