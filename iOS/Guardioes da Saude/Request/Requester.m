// Igor Morais

#import "Requester.h"

@implementation Requester

- (void) doGet: (NSString *) url
        header: (NSDictionary *) headerMap
     parameter: (id) parameter
         start: (void (^)()) onStart
         error: (void (^)(AFHTTPRequestOperation * request, NSError * error)) onError
       success: (void (^)(AFHTTPRequestOperation * request, id response)) onSuccess {
    
    onStart();
    
    AFHTTPRequestOperationManager * manager = [AFHTTPRequestOperationManager manager];
    
    for (NSString * key in headerMap) {
        
        [manager.requestSerializer setValue: [headerMap objectForKey: key]
                         forHTTPHeaderField: key];
    }
    
    [manager GET: url
      parameters: parameter
         success: ^(AFHTTPRequestOperation * request, id response) {
             
             onSuccess(request, response);
         }
     
         failure: ^(AFHTTPRequestOperation * request, NSError * error) {
             
             onError(request, error);
         }
    ];
}

- (void) doGet: (NSString *) url
        header: (NSDictionary *) headerMap
     parameter: (id) parameter
    serializer: (AFHTTPResponseSerializer *) serializer
         start: (void (^)()) onStart
         error: (void (^)(AFHTTPRequestOperation * request, NSError * error)) onError
       success: (void (^)(AFHTTPRequestOperation * request, id response)) onSuccess {
    
    onStart();
    
    AFHTTPRequestOperationManager * manager = [AFHTTPRequestOperationManager manager];
    
    manager.responseSerializer = serializer;
    
    for (NSString * key in headerMap) {
        
        [manager.requestSerializer setValue: [headerMap objectForKey: key]
                         forHTTPHeaderField: key];
    }
    
    [manager GET: url
      parameters: parameter
         success: ^(AFHTTPRequestOperation * request, id response) {
             
             onSuccess(request, response);
         }
     
         failure: ^(AFHTTPRequestOperation * request, NSError * error) {
             
             onError(request, error);
         }
     ];
}

- (void) doPost: (NSString *) url
         header: (NSDictionary *) headerMap
      parameter: (id) parameter
          start: (void (^)()) onStart
          error: (void (^)(AFHTTPRequestOperation * request, NSError * error)) onError
        success: (void (^)(AFHTTPRequestOperation * request, id response)) onSuccess {
    
    onStart();
    
    AFHTTPRequestOperationManager * manager = [AFHTTPRequestOperationManager manager];
    
    for (NSString * key in headerMap) {
        
        [manager.requestSerializer setValue: [headerMap objectForKey: key]
                         forHTTPHeaderField: key];
    }
    
    [manager POST: url
       parameters: parameter
          success: ^(AFHTTPRequestOperation * request, id response) {
              
              onSuccess(request, response);
          }
     
          failure: ^(AFHTTPRequestOperation * request, NSError * error) {
              
              onError(request, error);
          }
     ];
}

- (void) doPost: (NSString *) url
         header: (NSDictionary *) headerMap
      parameter: (id) parameter
     serializer: (AFHTTPResponseSerializer *) serializer
          start: (void (^)()) onStart
          error: (void (^)(AFHTTPRequestOperation * request, NSError * error)) onError
        success: (void (^)(AFHTTPRequestOperation * request, id response)) onSuccess {
    
    onStart();
    
    AFHTTPRequestOperationManager * manager = [AFHTTPRequestOperationManager manager];
    
    manager.responseSerializer = serializer;
    
    for (NSString * key in headerMap) {
        
        [manager.requestSerializer setValue: [headerMap objectForKey: key]
                         forHTTPHeaderField: key];
    }
    
    [manager POST: url
       parameters: parameter
          success: ^(AFHTTPRequestOperation * request, id response) {
              
              onSuccess(request, response);
          }
     
          failure: ^(AFHTTPRequestOperation * request, NSError * error) {
              
              onError(request, error);
          }
     ];
}

@end
