// Igor Morais

#import "Constants.h"
#import "StatusCode.h"
#import "Sumary.h"
#import "SumaryCalendar.h"
#import "SumaryGraph.h"
#import "UserRequester.h"

@implementation UserRequester

- (void) login: (User *) user
       onStart: (Start) onStart
       onError: (Error) onError
     onSuccess: (Success) onSuccess {
    
    NSString * url = [NSString stringWithFormat: @"%@/user/login", Url];
    
    NSDictionary * paramMap = @{ @"email": user.email,
                                 @"password": user.password };
    
    [self doPost: url
          header: nil
       parameter: paramMap
           start: onStart
     
           error: ^(AFHTTPRequestOperation * request, NSError * error) {
               
               onError(error);
           }
     
         success: ^(AFHTTPRequestOperation * request, id response) {
             
             if ([request.response statusCode] == Ok) {
                 
                 if ([response[@"error"] boolValue]) {
                     
                     onError(nil);
                 
                 } else {
                     
                     NSDictionary * paramMap = response[@"user"];
                     
                     User * user = [User getInstance];
                     
                     user.nick = paramMap[@"nick"];
                     user.email = paramMap[@"email"];
                     user.gender = paramMap[@"gender"];
                     user.picture = paramMap[@"picture"];
                     user.idUser =  paramMap[@"id"];
                     user.race = paramMap[@"race"];
                     user.dob = paramMap[@"dob"];
                     user.user_token = response[@"token"];
                     user.hashtag = paramMap[@"hashtags"];
                     user.household = paramMap[@"household"];
                     user.survey = paramMap[@"surveys"];
                     
                     onSuccess(user);
                 }
             
             } else {
                 
                 onError(nil);
             }
         }
    ];
}

- (void) createAccount: (User *) user
               onStart: (Start) onStart
               onError: (Error) onError
             onSuccess: (Success) onSuccess {
}

- (void) getSummary: (User *) user
            onStart: (Start) onStart
            onError: (Error) onError
          onSuccess: (Success) onSuccess {
    
    NSString * url = [NSString stringWithFormat: @"%@/user/survey/summary", Url];
    
    [self doGet: url
         header: @{ @"user_token": user.user_token }
      parameter: nil
          start: onStart
     
          error: ^(AFHTTPRequestOperation * request, NSError * error) {
              
              onError(error);
          }
     
        success: ^(AFHTTPRequestOperation * request, id response) {
            
            if ([request.response statusCode] == Ok) {
                
                NSDictionary * paramMap = response[@"data"];
                
                Sumary * sumary = [[Sumary alloc] init];
                
                sumary.noSymptom = [paramMap[@"no_symptom"] intValue];
                sumary.symptom = [paramMap[@"symptom"] intValue];
                sumary.total = [paramMap[@"total"] intValue];
                
                onSuccess(sumary);
                
            } else {
                
                onError(nil);
            }
        }
     ];
}

- (void) getSummary: (User *) user
              month: (NSInteger) month
               year: (NSInteger) year
            onStart: (Start) onStart
            onError: (Error) onError
          onSuccess: (Success) onSuccess {
    
    NSString * url = [NSString stringWithFormat: @"%@/user/calendar/month?", Url];
    
    NSDictionary * paramMap = @{ @"month": [NSNumber numberWithInteger: month],
                                 @"year": [NSNumber numberWithInteger: year] };
    
    [self doGet: url
         header: @{ @"user_token": user.user_token }
      parameter: paramMap
          start: onStart
     
          error: ^(AFHTTPRequestOperation * request, NSError * error) {
              
              onError(error);
          }
     
        success: ^(AFHTTPRequestOperation * request, id response) {
            
            if ([request.response statusCode] == Ok) {
                
                if ([response[@"error"] boolValue]) {
                    
                    onError(nil);
                    
                } else {
                    
                    NSMutableDictionary * sumaryCalendarMap = [NSMutableDictionary dictionary];
                    
                    NSDictionary * paramMap = response[@"data"];
                    
                    for (NSDictionary * key in paramMap) {
                        
                        NSDictionary * sumaryMap = key[@"_id"];
                        
                        NSString * keyMap = [NSString stringWithFormat: @"%@-%@-%@",
                                          [sumaryMap[@"day"] stringValue], [sumaryMap[@"month"] stringValue], [sumaryMap[@"year"] stringValue]];
                        
                        SumaryCalendar * sumaryCalendar = [sumaryCalendarMap objectForKey: keyMap];
                                                           
                        if (sumaryCalendar) {
                            
                            if ([sumaryMap[@"no_symptom"] isEqualToString: @"N"]) {
                                sumaryCalendar.symptomAmount = [key[@"count"] intValue];
                                
                            } else {
                                sumaryCalendar.noSymptomAmount = [key[@"count"] intValue];
                            }
                                                               
                        } else {
                            
                            SumaryCalendar * sumaryCalendar = [[SumaryCalendar alloc] init];
                            
                            sumaryCalendar.day = [sumaryMap[@"day"] intValue];
                            sumaryCalendar.month = [sumaryMap[@"month"] intValue];
                            sumaryCalendar.year = [sumaryMap[@"year"] intValue];
                            
                            if ([sumaryMap[@"no_symptom"] isEqualToString: @"N"]) {
                                sumaryCalendar.symptomAmount = [key[@"count"] intValue];
                                
                            } else {
                                sumaryCalendar.noSymptomAmount = [key[@"count"] intValue];
                            }
                            
                            [sumaryCalendarMap setValue: sumaryCalendar forKey: keyMap];
                        }
                    }
                    
                    onSuccess(sumaryCalendarMap);
                }
            
            } else {
                
                onError(nil);
            }
        }
     ];
}

- (void) getSummary: (User *) user
               year: (int) year
            onStart: (Start) onStart
            onError: (Error) onError
          onSuccess: (Success) onSuccess {
    
    NSString * url = [NSString stringWithFormat: @"%@/user/calendar/year?", Url];
    
    [self doGet: url
         header: @{ @"user_token": user.user_token }
      parameter: @{ @"year": [NSNumber numberWithInt: year] }
          start: onStart
     
          error: ^(AFHTTPRequestOperation * request, NSError * error) {
              
              onError(error);
          }
     
        success: ^(AFHTTPRequestOperation * request, id response) {
            
            if ([request.response statusCode] == Ok) {
                
                if ([response[@"error"] boolValue]) {
                    
                    onError(nil);
                    
                } else {
                    
                    NSMutableDictionary * sumaryGraphMap = [NSMutableDictionary dictionary];
                    
                    for (NSDictionary * jsonMap in response[@"data"]) {
                        
                        SumaryGraph * sumaryGraph = [[SumaryGraph alloc] init];
                        
                        NSDictionary * sumaryJson = jsonMap[@"_id"];
                        
                        sumaryGraph.month = [sumaryJson[@"month"] intValue];
                        sumaryGraph.year = [sumaryJson[@"year"] intValue];
                        sumaryGraph.count = [jsonMap[@"count"] intValue];
                        sumaryGraph.percent = [jsonMap[@"percent"] floatValue];
                        
                        [sumaryGraphMap setValue: sumaryGraph forKey: sumaryJson[@"month"]];
                    }
                         
                    onSuccess(sumaryGraphMap);
                }
            
            } else {
                
                onError(nil);
            }
        }
     ];
}

@end
